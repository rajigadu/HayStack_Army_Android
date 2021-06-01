package com.haystack.app.`in`.army.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.haystack.app.`in`.army.R
import com.haystack.app.`in`.army.databinding.FragmentManualSearchBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.countries.Countries
import com.haystack.app.`in`.army.network.response.search_events.SearchByEvent
import com.haystack.app.`in`.army.network.response.states.States
import com.haystack.app.`in`.army.utils.AppConstants.ARG_SERIALIZABLE
import com.haystack.app.`in`.army.utils.Extensions
import com.haystack.app.`in`.army.utils.Extensions.longSnackBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManualSearch: Fragment() {

    private lateinit var binding: FragmentManualSearchBinding
    private var selectedCountry: String? = ""
    private var listStates = arrayListOf<String>()
    private var searchEvent: SearchByEvent? = null
    private var lastClickTime: Long = 0

    private var listCountries = arrayListOf<String>()

    private var zipCode: String? = null
    private var selectedState = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManualSearchBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCountriesList()

        searchEvent = arguments?.getSerializable(ARG_SERIALIZABLE) as SearchByEvent
        Log.e("TAG", "searchEvent: $searchEvent")

        clickListeners()

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {

        binding.toolbarManualSearch.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnContinue.setOnClickListener {
            if (validated()){
                val bundle = bundleOf(ARG_SERIALIZABLE to searchEvent)
                findNavController().navigate(R.id.action_manualSearch_to_dateRangeFragment, bundle)
            }
        }

        binding.inputCountry.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_UP -> {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                        return@setOnTouchListener false
                    }
                    lastClickTime = SystemClock.elapsedRealtime()
                    showCountriesListDialogView()
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }

        binding.inputState.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_UP -> {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                        return@setOnTouchListener false
                    }
                    lastClickTime = SystemClock.elapsedRealtime()
                    showStatesListDialog()
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }
    }

    private fun validated(): Boolean {
        searchEvent?.country = binding.inputCountry.text.toString().trim()
        searchEvent?.state = binding.inputState.text.toString().trim()
        zipCode = binding.inputZipCode.text.toString().trim()
        searchEvent?.city = binding.inputCity.text.toString().trim()


        when {
            searchEvent?.country!!.isEmpty() -> {
                longSnackBar("Select Your Country", binding.constraintManualSearch)
                return false
            }

            searchEvent?.state!!.isEmpty() -> {
                longSnackBar("Select Your State", binding.constraintManualSearch)
                return false
            }

            searchEvent?.city!!.isEmpty() -> {
                longSnackBar("Enter Your City", binding.constraintManualSearch)
                return false
            }

            zipCode?.isEmpty()!! -> {
                longSnackBar("Enter Zip code", binding.constraintManualSearch)
                return false
            }

            else -> return true
        }

    }

    private fun showStatesListDialog() {
        var array = arrayOf<String>()
        array = listStates.toArray(array)

        MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle("Select Event State")
            .setCancelable(false)
            .setPositiveButton("Ok"){ dialog, which ->
                binding.inputState.setText(selectedState)
            }
            .setSingleChoiceItems(array,-1){ dialog, which ->
                selectedState = array[which]
            }
            .show()
    }

    private fun getCountriesList() {
        Repository.getAllCountries().enqueue(object : Callback<Countries>{
            override fun onResponse(call: Call<Countries>, response: Response<Countries>) {
                try {

                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){
                            if (response.body()?.data?.size!! > 0){
                                listCountries.clear()
                                for (elements in response.body()?.data!!){
                                    listCountries.add(elements.name)
                                }
                            }
                        }
                    }

                }catch (e: Exception){e.printStackTrace()}
            }

            override fun onFailure(call: Call<Countries>, t: Throwable) {
                Extensions.showErrorResponse(t, binding.constraintManualSearch)
            }

        })
    }


    private fun showCountriesListDialogView() {
        var array = arrayOf<String>()
        array = listCountries.toArray(array)

        MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle("Select Event Country")
            .setCancelable(false)
            .setPositiveButton("Ok"){ dialog, which ->
                //getStatesList()
                binding.inputCountry.setText(selectedCountry)
            }
            .setSingleChoiceItems(array,-1){ dialog, which ->
                selectedCountry = array[which]
            }
            .show()
    }

    private fun getStatesList() {
        Repository.getAllStatesOfTheCountry(selectedCountry!!).enqueue(
            object : Callback<States> {
                override fun onResponse(call: Call<States>, response: Response<States>) {
                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){
                                if (response.body()?.data?.size!! > 0){
                                    listStates.clear()
                                    for (item in response.body()?.data!!){
                                        listStates.add(item.name)
                                    }
                                }
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                }

                override fun onFailure(call: Call<States>, t: Throwable) {
                    Extensions.showErrorResponse(t, binding.constraintManualSearch)
                }

            })
    }

    override fun onResume() {
        super.onResume()
        getStatesList()
    }
}