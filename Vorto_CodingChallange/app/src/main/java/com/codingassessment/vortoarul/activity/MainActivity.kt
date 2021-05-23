package com.codingassessment.vortoarul.activity

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.codingassessment.vortoarul.R
import com.codingassessment.vortoarul.model.BusinessDetails
import com.codingassessment.vortoarul.utils.checkLocationPermission
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), OnMapReadyCallback,
        LocationListener {
    private var mMap: GoogleMap? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mCurrentLocation: Location? = null
    private var mBusinessDetails: BusinessDetails? = null
    private lateinit var mLocationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        back_button.setOnClickListener{ finish() }
        createLocationRequest()
        initializeCurrentLocationTracker()
        getLocation()
        intent.getBundleExtra(BUNDLE)?.getParcelable<BusinessDetails>(BUSINESS_DETAIL)?.let {
            mBusinessDetails = it
        }
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initializeCurrentLocationTracker() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkLocationPermission()) {
            initializeLocationListener()
        }
    }

    private fun initializeLocationListener() {
        if (checkLocationPermission()) {
            mFusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (mCurrentLocation == null) {
                            mCurrentLocation = location
                            addMarkers(mCurrentLocation?.latitude, mCurrentLocation?.longitude, "You", "")
                        }
                    }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (mCurrentLocation != null) addMarkers(mCurrentLocation?.latitude, mCurrentLocation?.longitude, "You", "")
        if (mBusinessDetails != null) addMarkers(mBusinessDetails?.coordinates?.latitude, mBusinessDetails?.coordinates?.longitude, mBusinessDetails?.name, mBusinessDetails?.display_phone)
    }

    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this@MainActivity,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        initializeLocationListener()
                    }
                } else {
                    checkLocationPermission()
                }
                return
            }
        }
    }

    private fun addMarkers(latitude: Double?, longitude: Double?, name: String?, snippet: String?) {
        latitude?.let { lat ->
            longitude?.let { lng ->
                val currentLoc = LatLng(lat, lng)
                mMap?.let {
                        it.addMarker(MarkerOptions()
                                .position(currentLoc)
                                .title(name)
                                .snippet(if ("You".equals(name, true)) name else snippet)
                                .icon(BitmapDescriptorFactory.defaultMarker(if ("You".equals(name, true)) BitmapDescriptorFactory.HUE_ORANGE else BitmapDescriptorFactory.HUE_GREEN))
                        ).showInfoWindow()
                    it.moveCamera(CameraUpdateFactory.newLatLng(currentLoc))
                    it.setMinZoomPreference(10.4f) // To increase for decrease map zoom level
                }
            }
        }
    }

    private fun createLocationRequest() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (locationRequest != null) {
            val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)
            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
            task.addOnSuccessListener {
                getLastKnownLocation()
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException){
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        exception.startResolutionForResult(this@MainActivity,
                                12)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }
    }

    /**
     * call this method for receive location
     * get location and give callback when successfully retrieve
     * function itself check location permission before access related methods
     *
     */
    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        initializeCurrentLocationTracker()
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 15f, this)
        }
    }

    companion object {
        const val BUNDLE = "bundle"
        const val BUSINESS_DETAIL = "business_detail"
    }

    override fun onLocationChanged(loc: Location) {
        if (mCurrentLocation == null) {
            mCurrentLocation = loc
            addMarkers(mCurrentLocation?.latitude, mCurrentLocation?.longitude, "You", "")
        }
        mLocationManager?.removeUpdates {  }
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}