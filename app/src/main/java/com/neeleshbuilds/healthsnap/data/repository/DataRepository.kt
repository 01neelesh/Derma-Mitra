package com.neeleshbuilds.healthsnap.data.repository



import com.neeleshbuilds.healthsnap.ui.places.Place
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepository @Inject constructor() {
    suspend fun getNearbyDermatologists(latitude: Double, longitude: Double): List<Place> =
        withContext(Dispatchers.IO) {
            val places = mutableListOf<Place>()
            try {
                val request = FindCurrentPlaceRequest.newInstance(
                    listOf(Field.NAME, Field.ADDRESS, Field.OPENING_HOURS)
                )
                // Mock implementation (replace with actual Places API call)
                places.add(Place("Dr. Jones Clinic", "123 Main St", true))
                places.add(Place("Skin Care Center", "456 Elm St", false))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            places
        }
}