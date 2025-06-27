package com.neeleshbuilds.healthsnap.ui.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neeleshbuilds.healthsnap.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsernameViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    fun saveUsername(username: String) {
        viewModelScope.launch {
            repository.saveUsername(username)
        }
    }
}