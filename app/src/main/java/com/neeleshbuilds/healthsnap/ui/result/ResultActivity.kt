package com.neeleshbuilds.healthsnap.ui.result

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.databinding.ActivityResultBinding
import com.neeleshbuilds.healthsnap.ui.doctor.DoctorActivity
import com.neeleshbuilds.healthsnap.ui.medicines.SkinMedicinesActivity
import com.neeleshbuilds.healthsnap.ui.more.MoreAboutDiseaseActivity
import com.neeleshbuilds.healthsnap.ui.places.PlacesActivity
import com.neeleshbuilds.healthsnap.ui.upload.UploadActivity
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Extract intent data
        val disease = intent.getStringExtra("disease") ?: getString(R.string.no_disease)
        val suspicionRate = intent.getStringExtra("suspicionRate") ?: "0%"
        val symptoms = intent.getStringExtra("symptoms") ?: getString(R.string.no_symptoms)

        // Update header and result
        binding.header.text = if (disease == getString(R.string.no_disease)) {
            getString(R.string.you_are_fine)
        } else {
            getString(R.string.analysis_result)
        }
        binding.result.text = disease
        binding.suspicionRate.text = getString(R.string.suspicion_rate, suspicionRate)
        binding.symptoms.text = getString(R.string.symptoms, symptoms)
        binding.resultImg.setImageResource(
            if (disease == getString(R.string.no_disease)) R.drawable.rating else R.drawable.ic_pharmacist
        )

        // Hide start FAB as per XML (visibility="gone")
        binding.start.visibility = View.GONE

        // Animate FABs and card
        listOf(
            binding.noContainer, binding.doctorsContainer, binding.medicinesContainer,
            binding.placesContainer, binding.backContainer
        ).forEachIndexed { index, container ->
            container.alpha = 0f
            container.translationY = 50f
            container.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setStartDelay((index * 100).toLong())
                .start()
        }

        // Click listeners with scale animation
        binding.no.setOnClickListener { view ->
            animateButton(view) {
                startActivity(Intent(this, UploadActivity::class.java))
                finish()
            }
        }

        binding.doctorsButton.setOnClickListener { view ->
            animateButton(view) {
                startActivity(Intent(this, DoctorActivity::class.java))
            }
        }

        binding.medicinesButton.setOnClickListener { view ->
            animateButton(view) {
                startActivity(Intent(this, SkinMedicinesActivity::class.java))
            }
        }

        binding.placesButton.setOnClickListener { view ->
            animateButton(view) {
                startActivity(Intent(this, PlacesActivity::class.java))
            }
        }

        binding.backButton.setOnClickListener { view ->
            animateButton(view) {
                startActivity(Intent(this, UploadActivity::class.java))
                finish()
            }
        }
    }

    private fun animateButton(view: View, onEnd: () -> Unit) {
        view.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .withEndAction { onEnd() }
                    .start()
            }
            .start()
    }
}