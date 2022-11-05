package com.example.notinstagram.interactions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import java.util.function.Consumer


class LocationHandler(private var owner: ComponentActivity) : LocationListener {

    companion object {
        const val CALLBACK_CODE = 1;
        val PERMISSIONS = arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION )
    }

    private val criteria = Criteria()
    private val service = Context.LOCATION_SERVICE

    private var manager: LocationManager? = null
    private var provider: String? = null

    private val permissionLauncher = owner.registerForActivityResult(
                                        ActivityResultContracts.RequestMultiplePermissions()
                                        ) { grantResults -> onRequestPermissionsResult(grantResults) }

    private var callback: Consumer<Address>? = null

    init {
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isSpeedRequired = false
        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isCostAllowed = false
        criteria.powerRequirement = Criteria.POWER_MEDIUM
    }

    private fun onRequestPermissionsResult(grantResults: Map<String, Boolean>?) {
        val hasPermissions = grantResults?.all { grantResult -> grantResult.value }
        if(hasPermissions == true){
            callback?.let { findLocation(it) }
        }
    }

    @SuppressLint("MissingPermission")
    fun findLocation(onSuccess: Consumer<Address>) {
        callback = onSuccess
        if(hasPermissions()) {
            manager = owner.getSystemService(service) as LocationManager?
            provider = manager?.getBestProvider(criteria, true)
            provider?.let { manager?.requestLocationUpdates(it, 1000L, 0f, this) }
        } else {
            permissionLauncher.launch(PERMISSIONS)
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in PERMISSIONS) {
            if ( ActivityCompat.checkSelfPermission(owner, permission)
                    != PackageManager.PERMISSION_GRANTED ) {
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onLocationChanged(location: Location) {
        manager?.removeUpdates(this)
        val geocoder = Geocoder(owner)
        val address = geocoder.getFromLocation(location.latitude, location.longitude, 1).first()
        callback?.accept(address)
    }

}