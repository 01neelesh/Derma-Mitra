package com.neeleshbuilds.healthsnap.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neeleshbuilds.healthsnap.data.db.entity.UserProfile
import com.neeleshbuilds.healthsnap.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    fun saveUserProfile(user: UserProfile) {
        viewModelScope.launch {
            try {
                repository.saveUserProfile(user)
                Log.d("ProfileViewModel", "User profile saved: ${user.username}")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving user profile", e)
            }
        }
    }

    suspend fun getUserProfile(username: String): UserProfile? {
        return try {
            repository.getUserProfile(username)
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Error fetching user profile", e)
            null
        }
    }
}