package com.neeleshbuilds.healthsnap.ui.places

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor() : ViewModel() {
    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> = _places

    fun fetchPlaces() {
        // Dummy data for dermatologists
        val dummyPlaces = listOf(
            Place(
                name = "Dr. Smith Dermatology",
                address = "123 Main St, City",
                isOpen = true
            ),
            Place(
                name = "SkinCare Clinic",
                address = "456 Oak Ave, Town",
                isOpen = false
            ),
            Place(
                name = "City Derm Center",
                address = "789 Pine Rd, Village",
                isOpen = true
            )
        )
        Log.d("PlaceViewModel", "Posting ${dummyPlaces.size} dummy places")
        _places.postValue(dummyPlaces)
    }
}