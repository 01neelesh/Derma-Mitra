package com.neeleshbuilds.healthsnap.ui.gender

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.neeleshbuilds.healthsnap.databinding.ActivityGenderBinding
import com.neeleshbuilds.healthsnap.ui.upload.UploadActivity
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGenderBinding
    private val viewModel: GenderViewModel by viewModels()
    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivityGenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animate main layout
        binding.main.alpha = 0f
        binding.main.translationY = 50f
        binding.main.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .start()

        // Animate gender cards
        listOf(binding.femaleCard, binding.maleCard).forEachIndexed { index, card ->
            card.alpha = 0f
            card.translationY = 50f
            card.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setStartDelay((index * 150).toLong())
                .start()
        }

        // Female card click
        binding.femaleCard.setOnClickListener {
            selectGender("female")
            animateCard(binding.femaleCard)
        }

        // Male card click
        binding.maleCard.setOnClickListener {
            selectGender("male")
            animateCard(binding.maleCard)
        }

        // Continue button click
        binding.continueButton.setOnClickListener { view ->
            animateButton(view) {
                selectedGender?.let { gender ->
                    viewModel.updateGender(gender)
                    startActivity(Intent(this, UploadActivity::class.java))
                    overridePendingTransition(0, android.R.anim.fade_out)
                    finish()
                } ?: Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectGender(gender: String) {
        selectedGender = gender
        // Update indicators
        binding.femaleIndicator.visibility = if (gender == "female") View.VISIBLE else View.INVISIBLE
        binding.maleIndicator.visibility = if (gender == "male") View.VISIBLE else View.INVISIBLE
        // Enable continue button
        binding.continueButton.isEnabled = true
    }

    private fun animateCard(card: View) {
        card.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                card.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun animateButton(view: View, onClick: () -> Unit) {
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