package com.neeleshbuilds.healthsnap.data.repository

import android.content.SharedPreferences
import com.neeleshbuilds.healthsnap.data.db.dao.UserDao
import com.neeleshbuilds.healthsnap.data.db.entity.UserProfile
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun saveUserProfile(user: UserProfile) {
        userDao.saveUserProfile(user)
    }

    suspend fun getUserProfile(username: String): UserProfile? {
        return userDao.getUserProfile(username)
    }

    fun getCurrentUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    suspend fun saveUsername(username: String) {
        sharedPreferences.edit().putString("username", username).apply()
        // Create empty UserProfile if not exists
        val existing = userDao.getUserProfile(username)
        if (existing == null) {
            userDao.saveUserProfile(UserProfile(
                id = 0,
                username = username,
                firstName = "",
                lastName = "",
                phoneNumber = "",
                age = 0,
                bloodGroup = "",
                gender = ""
            ))
        }
    }
}