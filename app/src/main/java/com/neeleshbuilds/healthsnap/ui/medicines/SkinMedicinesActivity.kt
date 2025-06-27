package com.neeleshbuilds.healthsnap.ui.medicines

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.databinding.ActivitySkinMedicinesBinding
import com.neeleshbuilds.healthsnap.ui.doctor.DoctorActivity
import com.neeleshbuilds.healthsnap.ui.places.PlacesActivity
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkinMedicinesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySkinMedicinesBinding
    private val viewModel: SkinMedicinesViewModel by viewModels()
    private lateinit var adapter: SkinMedicineAdapter
    private val selectedTreatments = mutableListOf<SkinMedicine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivitySkinMedicinesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        adapter = SkinMedicineAdapter(viewModel.getMedicines()) { medicine ->
            if (selectedTreatments.contains(medicine)) {
                selectedTreatments.remove(medicine)
                Toast.makeText(this, "${getString(medicine.nameResId)} deselected", Toast.LENGTH_SHORT).show()
            } else {
                selectedTreatments.add(medicine)
                Toast.makeText(this, "${getString(medicine.nameResId)} selected", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Animate cards
        listOf(
            binding.categoryChips.parent.parent as View,
            binding.recyclerView.parent.parent as View,
            binding.pharmacyButton.parent.parent as View
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

        // Category chip filtering
        binding.categoryChips.setOnCheckedChangeListener { group, checkedId ->
            val category = when (checkedId) {
                R.id.chip_all -> "All"
                R.id.chip_topical -> "Topical"
                R.id.chip_oral -> "Oral"
                R.id.chip_natural -> "Natural"
                R.id.chip_procedure -> "Procedure"
                else -> "All"
            }
            adapter.updateMedicines(viewModel.getMedicines(category))
            (group.findViewById<Chip>(checkedId))?.let { chip ->
                animateChip(chip)
            }
        }

        // Button click listeners with animations
        binding.filterButton.setOnClickListener { view ->
            animateButton(view) {
                Toast.makeText(this, "Filter options not implemented yet", Toast.LENGTH_SHORT).show()
            }
        }

        binding.pharmacyButton.setOnClickListener { view ->
            animateButton(view) {
                startActivity(Intent(this, PlacesActivity::class.java))
            }
        }

        binding.consultationButton.setOnClickListener { view ->
            animateButton(view) {
                startActivity(Intent(this, DoctorActivity::class.java))
            }
        }

        binding.backButton.setOnClickListener { view ->
            animateButton(view) {
                finish()
            }
        }

        binding.saveTreatmentsButton.setOnClickListener { view ->
            animateButton(view) {
                if (selectedTreatments.isNotEmpty()) {
                    viewModel.saveSelectedTreatments(selectedTreatments)
                    Toast.makeText(this, "Treatments saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No treatments selected", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    private fun animateChip(chip: Chip) {
        chip.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(100)
            .withEndAction {
                chip.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }
}