package com.neeleshbuilds.healthsnap.ui.more

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.databinding.ActivityMoreAboutDiseaseBinding
import com.neeleshbuilds.healthsnap.ui.result.ResultActivity
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreAboutDiseaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoreAboutDiseaseBinding
    private val viewModel: MoreAboutDiseaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivityMoreAboutDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val disease = intent.getStringExtra("disease") ?: getString(R.string.no_disease)
        val details = viewModel.getDiseaseDetails(disease)

        binding.header.text = getString(R.string.about_disease, disease)
        binding.content.text = details

        binding.back.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("disease", disease)
                putExtra("suspicionRate", "N/A") // Placeholder
                putExtra("symptoms", "N/A") // Placeholder
            }
            startActivity(intent)
            finish()
        }
    }
}