package com.neeleshbuilds.healthsnap.ui.places

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.databinding.ActivityPlacesBinding
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlacesBinding
    private val viewModel: PlaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PlaceAdapter { place ->
            Log.d("PlacesActivity", "Clicked place: ${place.name}")
            // TODO: Add navigation or intent (e.g., open maps like in CoronaVirus)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.places.observe(this) { places ->
            Log.d("PlacesActivity", "Received ${places.size} places")
            adapter.submitList(places)
        }

        // Fetch dummy data directly (bypass location permission for testing)
        viewModel.fetchPlaces()

        binding.backButton.setOnClickListener { finish() }
    }
}