package com.neeleshbuilds.healthsnap.ui.doctor

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.neeleshbuilds.healthsnap.databinding.ActivityDoctorBinding
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorBinding
    private val viewModel: DoctorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = DoctorAdapter(viewModel.getDoctors())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.backButton.setOnClickListener { finish() }
    }
}