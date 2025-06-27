package com.neeleshbuilds.healthsnap.ui.doctor

data class DoctorItem(
    val name: String,
    val specialtyResId: Int,
    val availabilityResId: Int,
    val isAvailable: Boolean,
    val rating: Float = 0f, // Added for sorting
    val distance: Float = Float.MAX_VALUE // Added for nearby filter
)