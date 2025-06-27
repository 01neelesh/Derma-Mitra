package com.neeleshbuilds.healthsnap.ui.gender

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neeleshbuilds.healthsnap.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    fun updateGender(gender: String) {
        viewModelScope.launch {
            val username = repository.getCurrentUsername()
            if (username.isNullOrEmpty()) {
                Log.e("GenderViewModel", "No username found")
                return@launch
            }
            val user = repository.getUserProfile(username)?.copy(gender = gender)
            if (user != null) {
                repository.saveUserProfile(user)
                Log.d("GenderViewModel", "Gender updated: $gender for $username")
            } else {
                Log.e("GenderViewModel", "User profile not found for $username")
            }
        }
    }
}