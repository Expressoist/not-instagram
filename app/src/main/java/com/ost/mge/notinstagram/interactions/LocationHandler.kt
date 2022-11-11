package com.ost.mge.notinstagram.interactions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import java.util.function.Consumer


class LocationHandler(private var owner: ComponentActivity) : LocationListener {

    companion object {
        val PERMISSIONS = arrayOf( Manifest.permission.ACCESS_FINE_LOCATION,
                                   Manifest.permission.ACCESS_COARSE_LOCATION )
    }

    enum class Failure { MISSING_PERMISSIONS, GRANT_RESULTS_RETURNED_NULL, NO_PROVIDER_AVAILABLE }

    private val criteria = Criteria()
    private var manager: LocationManager? = null
    private var provider: String? = null
    private var successCallback: Consumer<Address>? = null
    private var failureCallback: Consumer<Failure>? = null
    private val permissionLauncher = owner.registerForActivityResult(
                                        ActivityResultContracts.RequestMultiplePermissions()
                                        ) { grantResults -> onPermissionsGranted(grantResults) }

    init {
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isSpeedRequired = false
        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isCostAllowed = false
        criteria.powerRequirement = Criteria.POWER_MEDIUM
    }

    fun findLocation(onSuccess: Consumer<Address>, onFailure: Consumer<Failure>) {
        successCallback = onSuccess
        failureCallback = onFailure
        if(hasPermissions()) {
            dispatchLocationService()
        } else {
            permissionLauncher.launch(PERMISSIONS)
        }
    }

    private fun onPermissionsGranted(grantResults: Map<String, Boolean>?) {
        grantResults?.also {
            val hasPermissions = it.all { grantResult -> grantResult.value }
            if(hasPermissions) {
                dispatchLocationService()
            } else {
                failureCallback?.accept(Failure.MISSING_PERMISSIONS)
            }
        }
        ?: failureCallback?.accept(Failure.GRANT_RESULTS_RETURNED_NULL)
    }

    @SuppressLint("MissingPermission")
    private fun dispatchLocationService() {
        manager = owner.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        provider = manager?.getBestProvider(criteria, true)
        provider?.also {
            manager?.requestLocationUpdates(it, 0L, 0f, this)
        }
        ?: failureCallback?.accept(Failure.NO_PROVIDER_AVAILABLE)
    }

    private fun hasPermissions(): Boolean {
        return PERMISSIONS.all{ permission ->
            ActivityCompat.checkSelfPermission(owner, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onLocationChanged(location: Location) {
        manager?.removeUpdates(this)
        val address = Geocoder(owner).getFromLocation(location.latitude, location.longitude, 1).first()
        successCallback?.accept(address)
    }
}