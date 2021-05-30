package com.android.app.`in`.haystack.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentSearchBinding
import com.android.app.`in`.haystack.manager.SessionManager
import com.android.app.`in`.haystack.network.response.search_events.SearchByEvent
import com.android.app.`in`.haystack.utils.AppConstants.ARG_SERIALIZABLE
import com.android.app.`in`.haystack.utils.AppConstants.AUTOCOMPLETE_REQUEST_CODE
import com.android.app.`in`.haystack.utils.AppConstants.PERMISSION_REQ_LOCATION
import com.android.app.`in`.haystack.utils.AppConstants.USER_LATITUDE
import com.android.app.`in`.haystack.utils.AppConstants.USER_LONGITUDE
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity.RESULT_ERROR
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class SearchFragment: Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    LocationListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {

    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentSearchBinding
    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private var currentmarker: Marker? = null
    private var fields: List<Place.Field>? = null

    private var searchEvent: SearchByEvent? = null
    private var nationWide: String? = "Yes"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateView()

        clickListeners()

    }

    private fun clickListeners() {

        binding.toolbarSearch.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.bottomSheetLayout.btnContinue.setOnClickListener {
            searchEvent?.nationWide = nationWide!!
            searchEvent?.searchType = "search"
            val bundle = bundleOf(ARG_SERIALIZABLE to searchEvent)
            findNavController().navigate(R.id.action_searchFragment_to_dateRangeFragment, bundle)
        }

        binding.bottomSheetLayout.btnManualSearch.setOnClickListener {
            searchEvent?.nationWide = nationWide!!
            searchEvent?.searchType = "Manual"
            val bundle = bundleOf(ARG_SERIALIZABLE to searchEvent)
            findNavController().navigate(R.id.action_searchFragment_to_manualSearch, bundle)
        }

        binding.bottomSheetLayout.nationWide.setOnCheckedChangeListener { compoundButton, isChecked ->
            nationWide = if (isChecked) "Yes"
            else "No"
        }
    }

    private fun initiateView() {

        if (!Places.isInitialized()){
            Places.initialize(
                requireContext().applicationContext, resources.getString(R.string.google_maps_key))
        }

        val placesClient = Places.createClient(requireContext())
        fields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )

        binding.addressSearchView.setOnClickListener {
            /*Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields!!
            ).build(requireContext()).apply {
                this@SearchFragment.startActivityForResult(this, AUTOCOMPLETE_REQUEST_CODE)
            }*/

            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields!!
            ).build(requireContext())
            resultLauncher.launch(intent)
        }

        searchEvent = arguments?.getSerializable(ARG_SERIALIZABLE) as SearchByEvent
        Log.e("TAG", "searchEvent: $searchEvent")

        BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        supportMapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        supportMapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        mMap.uiSettings.isZoomControlsEnabled = true

        setUpMap()
    }

    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQ_LOCATION)
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnCameraMoveListener(this)
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraIdleListener(this)
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->

            if (location != null){
                lastLocation = location
                val lat = SessionManager.instance.sPreference.getString(USER_LATITUDE, "")
                val lon = SessionManager.instance.sPreference.getString(USER_LONGITUDE, "")
                //Log.e("TAG", "lat: $lat  lon: $lon")
                //val currentLatLong = LatLng(location.latitude, location.longitude)
                val currentLatLong = LatLng(lat!!.toDouble(), lon!!.toDouble())
                setLocationAddress(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 16f))
            }
        }
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val place = Autocomplete.getPlaceFromIntent(data!!)

            //setLocationAddress(place.latLng!!)
            //mMap.clear()
        }
        else if (result.resultCode == RESULT_ERROR){
            val status = Autocomplete.getStatusFromIntent(result.data!!)
        }
    }

    private fun setLocationAddress(currentLatLong: LatLng) {
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        var addresses: List<Address>? = null
        var addressLine: String? = null

        try {

            addresses = geocoder.getFromLocation(
                    currentLatLong.latitude,
            currentLatLong.longitude,
            1
            )
            addressLine = addresses[0].getAddressLine(0)

        }catch (e: Exception){e.printStackTrace()}

        if (addressLine != null){
            placeMarkerOnMap(currentLatLong, addressLine!!)
            binding.addressSearchView.setQuery(addressLine, true)
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng, addressLine: String) {

        if (currentmarker == null){
            val markerOptions = MarkerOptions().position(currentLatLong)
            markerOptions.title(addressLine)
            markerOptions.icon(bitmapDescriptor())
            currentmarker = mMap.addMarker(markerOptions)

        }else{
            currentmarker?.title = addressLine
            currentmarker?.position = currentLatLong
        }
    }

    private fun bitmapDescriptor(): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.app_logo)

        vectorDrawable?.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onMarkerClick(marker: Marker) = false

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }

    override fun onLocationChanged(location: Location) {
        var latLng = LatLng(location.latitude, location.longitude)
        setLocationAddress(latLng)

        mMap.setOnCameraIdleListener {
            if (currentmarker != null){
                currentmarker?.remove()
                latLng = currentmarker?.position!!
                setLocationAddress(latLng)
            }
        }
    }

    override fun onCameraMove() {}

    override fun onCameraIdle() {
        try {

            val currentLatLong = LatLng(
                mMap.cameraPosition.target.latitude,
                mMap.cameraPosition.target.longitude
            )

            setLocationAddress(currentLatLong)
        }
        catch (e: IOException) {e.printStackTrace()}
        catch (e: IndexOutOfBoundsException) {e.printStackTrace()}
    }

    override fun onCameraMoveStarted(p0: Int) {}
}

