package com.neeleshbuilds.healthsnap.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.neeleshbuilds.healthsnap.data.db.AppDatabase
import com.neeleshbuilds.healthsnap.data.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "skin-care-db"
        ).build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("DermaMitraPrefs", Context.MODE_PRIVATE)
    }

//    @Provides
//    fun provideSkinMedicineDao(db: AppDatabase): SkinMedicineDao {
//        return db.skinMedicineDao()
//    }
}