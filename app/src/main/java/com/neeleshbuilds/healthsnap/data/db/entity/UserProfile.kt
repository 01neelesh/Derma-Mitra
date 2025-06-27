package com.neeleshbuilds.healthsnap.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val age: Int,
    val bloodGroup: String,
    val gender: String
)