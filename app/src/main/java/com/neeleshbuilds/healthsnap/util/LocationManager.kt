package com.neeleshbuilds.healthsnap.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject
import androidx.core.content.edit

class LocationManager @Inject constructor(
    private val context: Context
) {
    private val locationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    suspend fun getLastLocation(): Pair<Double, Double>? = withContext(Dispatchers.IO) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@withContext null
            }
            val location = locationClient.lastLocation.await()
            location?.let {
                context.getSharedPreferences("DermaMitraPrefs", Context.MODE_PRIVATE)
                    .edit() {
                        putFloat("latitude", it.latitude.toFloat())
                            .putFloat("longitude", it.longitude.toFloat())
                    }
                return@withContext Pair(it.latitude, it.longitude)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }
}