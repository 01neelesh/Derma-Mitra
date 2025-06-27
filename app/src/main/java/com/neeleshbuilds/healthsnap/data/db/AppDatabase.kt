package com.neeleshbuilds.healthsnap.data.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.neeleshbuilds.healthsnap.data.db.dao.UserDao

import com.neeleshbuilds.healthsnap.data.db.entity.UserProfile

@Database(entities = [UserProfile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

}