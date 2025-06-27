package com.neeleshbuilds.healthsnap.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.databinding.ActivityUploadBinding
import com.neeleshbuilds.healthsnap.ui.doctor.DoctorActivity
import com.neeleshbuilds.healthsnap.ui.medicines.SkinMedicinesActivity
import com.neeleshbuilds.healthsnap.ui.places.PlacesActivity
import com.neeleshbuilds.healthsnap.ui.result.LoadingResultActivity
import com.neeleshbuilds.healthsnap.ui.result.ResultActivity
import com.neeleshbuilds.healthsnap.ui.username.UsernameActivity
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityUploadBinding
    private val viewModel: UploadViewModel by viewModels()
    private var bitmap: Bitmap? = null
    private var lastClickTime = 0L
    private val CLICK_DEBOUNCE = 500L

    private val cameraPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        Log.d("UploadActivity", "Camera permission granted: $granted")
        if (granted) {
            launchCamera()
        } else {
            android.widget.Toast.makeText(this, "Camera permission denied", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private val imageCapture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
        Log.d("UploadActivity", "Image capture result: ${result != null}")
        bitmap = result
        updateImagePreview()
    }

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        Log.d("UploadActivity", "Image picker result: $uri")
        uri?.let {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                updateImagePreview()
            } catch (e: Exception) {
                Log.e("UploadActivity", "Error loading image: ${e.message}")
                android.widget.Toast.makeText(this, "Failed to load image", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Set username in nav drawer
        val username = getSharedPreferences("SkinCarePrefs", MODE_PRIVATE)
            .getString("username", getString(R.string.no_name))
        binding.navdrawer.getHeaderView(0).findViewById<TextView>(R.id.navtext)
            .text = username

        // Navigation drawer
        binding.toolbar.setNavigationOnClickListener {
            if (System.currentTimeMillis() - lastClickTime > CLICK_DEBOUNCE) {
                lastClickTime = System.currentTimeMillis()
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }
        binding.navdrawer.setNavigationItemSelectedListener(this)

        // Animate cards
        listOf(
            binding.textView1.parent.parent as View,
            binding.skinImg.parent.parent.parent as View,
            binding.take.parent.parent as View,
            binding.recentAnalysisCard
        ).forEachIndexed { index, card ->
            card.alpha = 0f
            card.translationY = 50f
            card.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setStartDelay((index * 100).toLong())
                .start()
        }

        // Button clicks with debounce and scale animation
        binding.take.setOnClickListener { view ->
            animateButton(view) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    launchCamera()
                } else {
                    Log.d("UploadActivity", "Requesting camera permission")
                    cameraPermissionRequest.launch(Manifest.permission.CAMERA)
                }
            }
        }

        binding.upload.setOnClickListener { view ->
            animateButton(view) {
                imagePicker.launch("image/*")
            }
        }

        binding.predict.setOnClickListener { view ->
            animateButton(view) {
                bitmap?.let { bmp ->
                    try {
                        val result = viewModel.analyzeImage(bmp)
                        viewModel.saveRecentAnalysis(result.first, result.second, result.third)
                        updateRecentAnalysisCard(result.first, viewModel.getLastAnalysisDate())
                        val intent = Intent(this, LoadingResultActivity::class.java).apply {
                            putExtra("disease", result.first)
                            putExtra("suspicionRate", result.second)
                            putExtra("symptoms", result.third)
                        }
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("UploadActivity", "Analysis error: ${e.message}")
                        android.widget.Toast.makeText(this, "Analysis failed: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    android.widget.Toast.makeText(this, getString(R.string.no_image_selected), android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.quickCaptureFab.setOnClickListener { view ->
            animateButton(view) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    launchCamera()
                } else {
                    cameraPermissionRequest.launch(Manifest.permission.CAMERA)
                }
            }
        }

        binding.viewHistoryButton.setOnClickListener { view ->
            animateButton(view) {
                val recentResult = viewModel.getRecentAnalysis()
                if (recentResult != null) {
                    val intent = Intent(this, ResultActivity::class.java).apply {
                        putExtra("disease", recentResult.first)
                        putExtra("suspicionRate", recentResult.second)
                        putExtra("symptoms", recentResult.third)
                    }
                    startActivity(intent)
                } else {
                    android.widget.Toast.makeText(this, getString(R.string.no_recent_analysis), android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Initialize recent analysis card
        val recentResult = viewModel.getRecentAnalysis()
        if (recentResult != null) {
            updateRecentAnalysisCard(recentResult.first, viewModel.getLastAnalysisDate())
        } else {
            binding.recentAnalysisCard.visibility = View.GONE
        }

        updateImagePreview()
    }

    private fun launchCamera() {
        Log.d("UploadActivity", "Launching camera")
        imageCapture.launch(null)
    }

    private fun updateImagePreview() {
        if (bitmap != null) {
            binding.skinImg.setImageBitmap(bitmap)
            binding.emptyStateOverlay.visibility = View.GONE
            binding.take.visibility = View.GONE
            binding.upload.visibility = View.GONE
            binding.predict.visibility = View.VISIBLE
        } else {
            binding.skinImg.setImageDrawable(null)
            binding.emptyStateOverlay.visibility = View.VISIBLE
            binding.take.visibility = View.VISIBLE
            binding.upload.visibility = View.VISIBLE
            binding.predict.visibility = View.GONE
        }
    }

    private fun updateRecentAnalysisCard(disease: String, date: String) {
        binding.recentAnalysisCard.visibility = View.VISIBLE
        binding.recentDiagnosis.text = disease
        binding.recentDate.text = getString(R.string.last_analysis_date).replace("None", date)
    }

    private fun animateButton(view: View, onClick: () -> Unit) {
        if (System.currentTimeMillis() - lastClickTime > CLICK_DEBOUNCE) {
            lastClickTime = System.currentTimeMillis()
            view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction { onClick() }
                        .start()
                }
                .start()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (System.currentTimeMillis() - lastClickTime > CLICK_DEBOUNCE) {
            lastClickTime = System.currentTimeMillis()
            when (item.itemId) {
                R.id.sign_out -> {
                    getSharedPreferences("SkinCarePrefs", MODE_PRIVATE).edit().clear().apply()
                    startActivity(Intent(this, UsernameActivity::class.java))
                    finish()
                }
                R.id.hospitals -> {
                    startActivity(Intent(this, PlacesActivity::class.java))
                }
                R.id.doctors -> {
                    startActivity(Intent(this, DoctorActivity::class.java))
                }
                R.id.medicines -> {
                    startActivity(Intent(this, SkinMedicinesActivity::class.java))
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            overridePendingTransition(0, R.anim.fade_out)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        binding.drawerLayout.closeDrawer(GravityCompat.START, false)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}