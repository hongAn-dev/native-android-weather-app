package com.example.weatherapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.weatherapp.ui.nav.Screen
import com.google.android.gms.location.LocationServices
import android.Manifest

class FusedLocationProviderClient {
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, onResult: (android.location.Location?) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Kiểm tra permission trước
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onResult(null) // Quyền chưa được cấp
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                onResult(location)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }


}