package com.android.app.`in`.haystack.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentHomeBinding
import com.android.app.`in`.haystack.utils.AppConstants.PERMISSION_REQ_LOCATION
import com.android.app.`in`.haystack.utils.Extensions.getUniqueRandomNumber
import com.android.app.`in`.haystack.utils.Extensions.showSnackBarSettings
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment: Fragment(), MultiplePermissionsListener {

    private lateinit var binding: FragmentHomeBinding
    private val permissionsLocation = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    private var geocoder: Geocoder? = null

    private var userLocation: String? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var latitude: Double? = null
    private var longitude: Double? = null





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateView()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initiateView() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(this)
            .onSameThread()
            .check()

        binding.searchView.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                ACTION_UP -> {
                    findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
                }
            }
            return@setOnTouchListener true
        }
        binding.btnMyEvents.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_myEvents)
        }
    }


    @SuppressLint("SimpleDateFormat")
    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).updateBottomNavChange(0)
        (activity as MainMenuActivity).showBottomNav()
        val sdf = SimpleDateFormat("EEEE,dd MMM")
        binding.currentDate.text = sdf.format(Date())
    }

    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
        if (p0!!.areAllPermissionsGranted()){
            getCurrentUserLocation()
        }
        else if (p0.isAnyPermissionPermanentlyDenied){
            showSnackBarSettings(
                requireContext(),
                "Permission was denied",
                "Settings",
                View.OnClickListener {
                    // Build intent that displays the App settings screen.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        Build.DISPLAY, null
                    )
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            )
        }
    }

    private fun getCurrentUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsLocation, PERMISSION_REQ_LOCATION)
            return
        }else{
            fusedLocationClient!!.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                lastLocation = task.result

                if (lastLocation == null) {
                    requestNewLocationData()
                }else{
                    latitude = lastLocation!!.latitude
                    longitude = lastLocation!!.longitude

                    if (latitude != null && longitude != null){
                        getUserAddress()
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserAddress() {
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        val addresses: List<Address> = geocoder!!.getFromLocation(
            latitude!!,
            longitude!!,
            1)
        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        userLocation = addresses[0].getAddressLine(0)

        val city: String = addresses[0].locality
        val state: String = addresses[0].adminArea
        val country: String = addresses[0].countryName
        val postalCode: String = addresses[0].postalCode
        val knownName: String = addresses[0].featureName // Only if available else return NULL

        binding.userLocation.text = "$city,$country"
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?,
        p1: PermissionToken?
    ) {
        p1?.continuePermissionRequest()
    }
}