package com.neeleshbuilds.healthsnap.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.neeleshbuilds.healthsnap.data.db.entity.UserProfile

@Dao
interface UserDao {
    @Upsert
    suspend fun saveUserProfile(user: UserProfile)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserProfile(username: String): UserProfile?
}