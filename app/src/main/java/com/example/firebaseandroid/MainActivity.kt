package com.technical.myapplication

import android.Manifest
import android.R
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {
    private var AddressText: TextView? = null
    private var LocationButton: Button? = null
    private var locationRequest: LocationRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        AddressText = findViewById(R.id.addressText)
//        LocationButton = findViewById(R.id.locationButton)
//
//        locationRequest = LocationRequest.create()
//
//        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest!!.interval = 5000
//        locationRequest!!.fastestInterval = 2000
//        LocationButton.setOnClickListener(View.OnClickListener { currentLocation })
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (isGPSEnabled) {
//                    currentLocation
//                } else {
//                    turnOnGPS()
//                }
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 2) {
//            if (resultCode == RESULT_OK) {
//                currentLocation
//            }
//        }
//    }
//
//    private val currentLocation: Unit
//        private get() {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (ActivityCompat.checkSelfPermission(
//                        this@MainActivity,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    if (isGPSEnabled) {
//                        LocationServices.getFusedLocationProviderClient(this@MainActivity)
//                            .requestLocationUpdates(locationRequest!!, object : LocationCallback() {
//                                override fun onLocationResult(locationResult: LocationResult) {
//                                    super.onLocationResult(locationResult)
//                                    LocationServices.getFusedLocationProviderClient(this@MainActivity)
//                                        .removeLocationUpdates(this)
//                                    if (locationResult != null && locationResult.locations.size > 0) {
//                                        val index = locationResult.locations.size - 1
//                                        val latitude = locationResult.locations[index].latitude
//                                        val longitude = locationResult.locations[index].longitude
//                                        AddressText!!.text =
//                                            "Latitude: $latitude\nLongitude: $longitude"
//                                    }
//                                }
//                            }, Looper.getMainLooper())
//                    } else {
//                        turnOnGPS()
//                    }
//                } else {
//                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
//                }
//            }
//        }
//
//    private fun turnOnGPS() {
//        val builder = LocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest!!)
//        builder.setAlwaysShow(true)
//        val result = LocationServices.getSettingsClient(
//            applicationContext
//        )
//            .checkLocationSettings(builder.build())
//        result.addOnCompleteListener { task ->
//            try {
//                val response = task.getResult(
//                    ApiException::class.java
//                )
//                Toast.makeText(this@MainActivity, "GPS is already tured on", Toast.LENGTH_SHORT)
//                    .show()
//            } catch (e: ApiException) {
//                when (e.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
//                        val resolvableApiException = e as ResolvableApiException
//                        resolvableApiException.startResolutionForResult(this@MainActivity, 2)
//                    } catch (ex: SendIntentException) {
//                        ex.printStackTrace()
//                    }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
//                }
//            }
//        }
//    }
//
//    private val isGPSEnabled: Boolean
//        private get() {
//            var locationManager: LocationManager? = null
//            var isEnabled = false
//            if (locationManager == null) {
//                locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//            }
//            isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            return isEnabled
//        }
}