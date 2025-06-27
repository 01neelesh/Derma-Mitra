package com.neeleshbuilds.healthsnap.ui.medicines

import androidx.lifecycle.ViewModel
import com.neeleshbuilds.healthsnap.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SkinMedicinesViewModel @Inject constructor() : ViewModel() {
    private val allMedicines = listOf(
        SkinMedicine(
            nameResId = R.string.medicine_antifungal_cream,
            detailsResId = R.string.medicine_antifungal_details,
            category = "Topical",
            iconResId = R.drawable.ic_cream
        ),
        SkinMedicine(
            nameResId = R.string.medicine_steroid_cream,
            detailsResId = R.string.medicine_steroid_details,
            category = "Topical",
            iconResId = R.drawable.ic_cream
        ),
        SkinMedicine(
            nameResId = R.string.medicine_antihistamine,
            detailsResId = R.string.medicine_antihistamine_details,
            category = "Oral",
            iconResId = R.drawable.ic_pill
        ),
        SkinMedicine(
            nameResId = R.string.medicine_natural_oil,
            detailsResId = R.string.medicine_natural_details,
            category = "Natural",
            iconResId = R.drawable.ic_leaf
        ),
        SkinMedicine(
            nameResId = R.string.medicine_laser_therapy,
            detailsResId = R.string.medicine_laser_details,
            category = "Procedure",
            iconResId = R.drawable.ic_medical
        )
    )

    fun getMedicines(category: String = "All"): List<SkinMedicine> {
        return if (category == "All") allMedicines else allMedicines.filter { it.category == category }
    }

    fun saveSelectedTreatments(selectedTreatments: List<SkinMedicine>) {
        // Placeholder: Save to SharedPreferences or database
        // For demo, log the action
        android.util.Log.d("SkinMedicinesViewModel", "Saved treatments: ${selectedTreatments.map { it.nameResId }}")
    }
}