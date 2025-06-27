package com.neeleshbuilds.healthsnap.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.neeleshbuilds.healthsnap.R
import com.neeleshbuilds.healthsnap.data.db.entity.UserProfile
import com.neeleshbuilds.healthsnap.databinding.ActivityProfileBinding
import com.neeleshbuilds.healthsnap.ui.gender.GenderActivity
import com.neeleshbuilds.healthsnap.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.updateLanguage(this, LanguageManager.getCurrentLanguage(this))
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animate main layout
        binding.main.alpha = 0f
        binding.main.translationY = 50f
        binding.main.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .start()

        // NumberPicker for age
        binding.agePicker.minValue = 1
        binding.agePicker.maxValue = 100
        binding.agePicker.value = 30
        binding.agePicker.wrapSelectorWheel = false

        // Blood group dropdown
        binding.bloodGroupInput.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bloodGroups)
        )
        binding.bloodGroupInput.setOnClickListener {
            binding.bloodGroupInput.showDropDown()
        }

        // Load existing profile (if any)
        val username = getSharedPreferences("DermaMitraPrefs", MODE_PRIVATE)
            .getString("username", "") ?: ""
        if (username.isNotEmpty()) {
            lifecycleScope.launch {
                viewModel.getUserProfile(username)?.let { user ->
                    binding.firstNameInput.setText(user.firstName)
                    binding.lastNameInput.setText(user.lastName)
                    binding.phoneInput.setText(user.phoneNumber)
                    binding.agePicker.value = user.age
                    binding.bloodGroupInput.setText(user.bloodGroup, false)
                }
            }
        } else {
            Toast.makeText(this, "No username found", Toast.LENGTH_SHORT).show()
        }

        // Save button with animation
        binding.saveButton.setOnClickListener { view ->
            animateButton(view) {
                saveProfile()
            }
        }
    }

    private fun saveProfile() {
        val firstName = binding.firstNameInput.text.toString().trim()
        val lastName = binding.lastNameInput.text.toString().trim()
        val phoneNumber = binding.phoneInput.text.toString().trim()
        val age = binding.agePicker.value
        val bloodGroup = binding.bloodGroupInput.text.toString().trim()

        // Validation
        if (TextUtils.isEmpty(firstName)) {
            binding.firstNameInput.error = getString(R.string.enter_first_name)
            return
        }
        if (TextUtils.isEmpty(lastName)) {
            binding.lastNameInput.error = getString(R.string.enter_last_name)
            return
        }
        if (TextUtils.isEmpty(phoneNumber) || !phoneNumber.matches(Regex("\\d{10}"))) {
            binding.phoneInput.error = getString(R.string.enter_valid_phone)
            return
        }
        if (TextUtils.isEmpty(bloodGroup) || !bloodGroups.contains(bloodGroup)) {
            binding.bloodGroupInput.error = getString(R.string.enter_blood_group)
            return
        }

        val username = getSharedPreferences("DermaMitraPrefs", MODE_PRIVATE)
            .getString("username", "") ?: ""
        if (username.isEmpty()) {
            Toast.makeText(this, "No username found", Toast.LENGTH_SHORT).show()
            return
        }

        val user = UserProfile(
            id = 0, // Let Room auto-generate
            username = username,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            age = age,
            bloodGroup = bloodGroup,
            gender = ""
        )

        viewModel.saveUserProfile(user)
        Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, GenderActivity::class.java))
        finish()
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
}