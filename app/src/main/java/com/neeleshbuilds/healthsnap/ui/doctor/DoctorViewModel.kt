package com.neeleshbuilds.healthsnap.ui.doctor
import androidx.lifecycle.ViewModel
import com.neeleshbuilds.healthsnap.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DoctorViewModel @Inject constructor() : ViewModel() {
    private val allDoctors = listOf(
        DoctorItem(
            name = "Dr. Smith",
            specialtyResId = R.string.specialty_dermatology,
            availabilityResId = R.string.availability_8am_9pm,
            isAvailable = true,
            rating = 4.8f,
            distance = 2.5f
        ),
        DoctorItem(
            name = "Dr. Patel",
            specialtyResId = R.string.specialty_cosmetic,
            availabilityResId = R.string.availability_5pm_3am,
            isAvailable = false,
            rating = 4.2f,
            distance = 10.0f
        ),
        DoctorItem(
            name = "Dr. Khan",
            specialtyResId = R.string.specialty_specialist,
            availabilityResId = R.string.availability_3pm_12am,
            isAvailable = true,
            rating = 4.5f,
            distance = 5.0f
        )
    )

    fun getDoctors(): List<DoctorItem> = allDoctors

    fun searchDoctors(query: String): List<DoctorItem> {
        if (query.isBlank()) return allDoctors
        return allDoctors.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.specialtyResId.let { resId ->
                        // Assuming context is needed to get string; for simplicity, match resource IDs
                        resId == R.string.specialty_dermatology && "dermatology".contains(query, ignoreCase = true) ||
                                resId == R.string.specialty_cosmetic && "cosmetic".contains(query, ignoreCase = true) ||
                                resId == R.string.specialty_specialist && "specialist".contains(query, ignoreCase = true)
                    }
        }
    }

    fun getNearbyDoctors(): List<DoctorItem> {
        return allDoctors.filter { it.distance <= 5.0f } // Example: 5km radius
    }

    fun getTopRatedDoctors(): List<DoctorItem> {
        return allDoctors.filter { it.rating >= 4.5f } // Example: Rating >= 4.5
    }

    fun getAvailableDoctors(): List<DoctorItem> {
        return allDoctors.filter { it.isAvailable }
    }

    fun sortDoctorsByRating(): List<DoctorItem> {
        return allDoctors.sortedByDescending { it.rating }
    }
}