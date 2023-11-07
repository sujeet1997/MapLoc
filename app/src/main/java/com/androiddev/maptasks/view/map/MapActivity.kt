package com.androiddev.maptasks.view.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.androiddev.maptasks.BuildConfig
import com.androiddev.maptasks.R
import com.androiddev.maptasks.databinding.ActivityMapBinding
import com.androiddev.maptasks.utils.AppController
import com.androiddev.maptasks.utils.PreferenceHelper
import com.androiddev.maptasks.view.login.LoginActivity
import com.androiddev.maptasks.view.profile.ProfileActivity
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.permissionx.guolindev.PermissionX

class MapActivity : AppCompatActivity(), Listener {
    private lateinit var binding: ActivityMapBinding

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    private lateinit var easyWayLocation: EasyWayLocation

    private lateinit var googleMap: GoogleMap
    lateinit var latLng: LatLng
    private lateinit var myMarker: Marker

    lateinit var logout: MenuItem
    lateinit var profile: MenuItem


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "MAP View"


        if (PreferenceHelper.getStringValue(this, BuildConfig.CustomerType) == BuildConfig.Parent){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        askPermission()
        if (this::easyWayLocation.isInitialized)
            easyWayLocation.startLocation()

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume() // needed to get the map to display immediately

        MapsInitializer.initialize(this)

        binding.captureLoc.setOnClickListener {
            getMAPLocation()

            binding.address.text = AppController.getAddress(this,latitude, longitude)
        }

    }

    @SuppressLint("MissingPermission")
    private fun getMAPLocation() {

        binding.mapView.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap
            val center = CameraUpdateFactory.newLatLng(LatLng(latitude, longitude))
            googleMap.moveCamera(center)
            googleMap.setMyLocationEnabled(false)
            googleMap.getUiSettings()
            .setScrollGesturesEnabled(false)
            latLng = LatLng(latitude, longitude)
            googleMap.getUiSettings().setZoomControlsEnabled(true)
            googleMap.getUiSettings().setRotateGesturesEnabled(false)
            googleMap.getUiSettings().setMapToolbarEnabled(false)

            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn())

            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            myMarker = googleMap.addMarker(
                MarkerOptions().position(latLng).draggable(false)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
            // For zooming automatically to the location of the marker
            val cameraPosition = CameraPosition.Builder().target(latLng).zoom(12f).build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            /*Set latlong on Marker where camera move position stop*/
            mMap.setOnCameraMoveListener {
            latLng = mMap.cameraPosition.target
            if (myMarker != null) myMarker.setPosition(latLng) else Log.d(
                "TAG",
                "Marker is null"
            )

        }


        })
    }

    private fun askPermission() {

        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList,
                    "Allow permission to access the location to access map",
                    "OK",
                    "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK",
                    "Cancel")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {

                    // Start Location access
                    locationAccess()

                } else {
                    // when permission denied
                    onBackPressed()
                }
            }

    }

    private fun locationAccess() {
        val request = LocationRequest.create().apply {
            interval = 60000
            fastestInterval = 30000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            maxWaitTime = 60000
        }
        easyWayLocation = EasyWayLocation(this, request, false, false, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                EasyWayLocation.LOCATION_SETTING_REQUEST_CODE -> easyWayLocation.onActivityResult(resultCode)
            }

        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()
        if (this::easyWayLocation.isInitialized)
            easyWayLocation.startLocation()
    }

    override fun onPause() {
        super.onPause()
        if (this::easyWayLocation.isInitialized)
            easyWayLocation.endUpdates()
    }

    override fun locationOn() {

    }

    override fun currentLocation(location: Location?) {
        if (location != null) {
            latitude = location.latitude
            longitude = location.longitude
        }

        Log.d("ebfbrfr","lat : " + latitude + " long : " + longitude)
    }

    override fun locationCancelled() {
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        logout = menu.findItem(R.id.logout)
        profile = menu.findItem(R.id.profile)

        if (PreferenceHelper.getStringValue(this, BuildConfig.CustomerType) == BuildConfig.Parent){
            if (::logout.isInitialized){
                logout.isVisible = false
            }

            if (::profile.isInitialized){
                profile.isVisible = false
            }
        }

            return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.logout ->{
//                GlobalScope.launch {
//                    dbHelper.deleteAllCustomerData()
//                }
                PreferenceHelper.clear(this)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            R.id.profile ->{
                startActivity(Intent(this, ProfileActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }
}