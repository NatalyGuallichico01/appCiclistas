package com.example.firebaseandroid

import android.Manifest
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.marginLeft
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*


enum class ProviderType {
    BASIC
}

class Profile : AppCompatActivity() {

    private val db = Firebase.firestore.collection("users")

    private var locationRequest: LocationRequest? = null

    private var latitudeCurrentUser = ""
    private var longitudeCurrentUser = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        locationRequest = LocationRequest.create()

        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = 5000
        locationRequest!!.fastestInterval = 2000

        // Setup fun
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        val userId = bundle?.getString("userId")

        setup(email ?: "", userId ?: "")
        getAllDataCurrentUser(userId ?: "")


        getCurrentLocationButton.setOnClickListener(View.OnClickListener { getCurrentLocation() })
        showCurrentLocationButton.setOnClickListener(View.OnClickListener {
            showCurrentLocation(
                latitudeCurrentUser,
                longitudeCurrentUser
            )
        })

        getCurrentLocation()

        showLocationsTable()

    }

    private fun setup(email: String, userId: String) {

        title = "Perfil"
        emailUser.text = email
        idUser.text = userId

        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {
                    getCurrentLocation()
                } else {
                    turnOnGPS()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                getCurrentLocation()
            }
        }
    }

    private fun getCurrentLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this@Profile,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(this@Profile)
                        .requestLocationUpdates(locationRequest!!, object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                super.onLocationResult(locationResult)
                                LocationServices.getFusedLocationProviderClient(this@Profile)
                                    .removeLocationUpdates(this)
                                if (locationResult != null && locationResult.locations.size > 0) {
                                    val index = locationResult.locations.size - 1
                                    val latitude1 = locationResult.locations[index].latitude
                                    val longitude1 = locationResult.locations[index].longitude

                                    latitudeCurrentUser = latitude1.toString()
                                    longitudeCurrentUser = longitude1.toString()

                                    latUser.text = latitudeCurrentUser
                                    longUser.text = longitudeCurrentUser

                                    println("user id: ${idUser.text}")

                                    //Firebase.firestore.collection("Locations")
                                    db.document(idUser.text.toString()).update(
                                        mapOf(
                                            "lati" to latitudeCurrentUser,
                                            "long" to longitudeCurrentUser
                                        )
                                    ).addOnCompleteListener { succesfulLocation() }
                                        .addOnFailureListener { error -> errorLocation(error) }

                                }
                            }
                        }, Looper.getMainLooper())
                } else {
                    turnOnGPS()
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }

    private fun turnOnGPS() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(
            applicationContext
        )
            .checkLocationSettings(builder.build())
        result.addOnCompleteListener(OnCompleteListener<LocationSettingsResponse?> { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                Toast.makeText(this@Profile, "GPS is already tured on", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: ApiException) {
                when (e.statusCode) {

                    // the location service is turn off
                    //  show a card to active the location
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(this@Profile, 2)
                    } catch (ex: SendIntentException) {
                        ex.printStackTrace()
                    }

                    // the mobile phone does not have the 'location service' installed
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        })
    }

    private fun isGPSEnabled(): Boolean {

        // check if the location service is active
        // if active, returns 'true'
        // if not, return 'false'

        // intitialize the LocationManager
        var locationManager: LocationManager? = null
        var isEnabled = false
        if (locationManager == null) {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isEnabled
    }


    private fun showCurrentLocation(latitude: String, longitude: String) {
        // Create a Uri from an intent string. Use the result to create an Intent.
        val gmmIntentUri = Uri.parse("google.streetview:cbll=$latitude,$longitude")

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
    }

    private fun succesfulLocation() {

        Toast.makeText(this, "Localización registrada", Toast.LENGTH_LONG).show()
    }

    private fun errorLocation(error: Exception) {

        Toast.makeText(
            this,
            "Se ha producido un error actualizando la localización ${error.message}",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun getAllDataCurrentUser(userId: String) {

        var locationData = mutableMapOf<String, Unit>()

        println("getAllData function")

        db.document(userId).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {

                val locationData = querySnapshot.data

                val lat_locationData = locationData?.get("lati") as String?
                val long_locationData = locationData?.get("long") as String?

                latUser.text = lat_locationData
                longUser.text = long_locationData

                latitudeCurrentUser = lat_locationData ?: ""
                longitudeCurrentUser = long_locationData ?: ""


            }
        }
    }

    private fun showLocationsTable() {

        val wrap = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val match = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        db.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {

                for (document in it) {

                    if (document.data["mail"].toString() !== emailUser.text.toString()) {

                        val text_userEmail = TextView(this)
                        text_userEmail.layoutParams = wrap

                        val btn_user = Button(this)
                        btn_user.layoutParams = wrap
                        btn_user.text = "Ver ubicación"
//                        btn_user.marginLeft = View.


                        var linearH = LinearLayout(this)
                        linearH.layoutParams = match

                        val userEmail = document.data["mail"]
                        val latUser = document.data["lati"]
                        val longUser = document.data["long"]

                        text_userEmail.text = userEmail.toString()
                        text_userEmail.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16F)

                        btn_user.setOnClickListener(View.OnClickListener {
                            showCurrentLocation(
                                latUser.toString(),
                                longUser.toString()
                            )
                        })

                        linearH.addView(text_userEmail)
                        linearH.addView(btn_user)
                        positionList__table.addView(linearH)


                    }


                }

            }

        }
    }

}