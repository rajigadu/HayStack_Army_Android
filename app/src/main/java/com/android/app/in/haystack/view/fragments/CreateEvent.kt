package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentCreateEventBinding
import com.android.app.`in`.haystack.network.response.event.Event
import com.android.app.`in`.haystack.utils.AppConstants.ARG_SERIALIZABLE
import com.android.app.`in`.haystack.utils.Extensions.showSnackBar
import com.android.app.`in`.haystack.view.activity.MainMenuActivity

class CreateEvent: Fragment() {

    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var events: Event


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateEventBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateEvent.setOnClickListener {
            if (validated()) {
                val bundle = bundleOf(ARG_SERIALIZABLE to events)
                findNavController().navigate(R.id.action_createEvent_to_categoriesFragment, bundle)
            }else{
                showSnackBar(binding.constraintCreateEvent, "Please fill all fields")
            }
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).updateBottomNavChange(4)
        (activity as MainMenuActivity).showBottomNav()
    }

    private fun validated(): Boolean {
        events = Event()
        events.eventName = binding.inputEventName.text.toString().trim()
        events.streetAddress = binding.inputStreetAddress.text.toString().trim()
        events.city = binding.inputCity.text.toString().trim()
        events.state = binding.inputState.text.toString().trim()
        events.zipCode = binding.inputZipCode.text.toString().trim()
        events.hostName = binding.inputHostName.text.toString().trim()
        events.contactInfo = binding.inputContactInfoOne.text.toString().trim() + "," +
                binding.inputContactInfoTwo.text.toString().trim()
        events.startTime = binding.startTime.text.toString().trim()
        events.endTime = binding.endTime.text.toString().trim()
        events.startDate = binding.inputStartDate.text.toString().trim()
        events.endDate = binding.inputEndDate.text.toString().trim()

        Log.e("TAG", "eventName: "+events.eventName)
        Log.e("TAG", "streetAddress: "+events.streetAddress)
        Log.e("TAG", "city: "+events.city)
        Log.e("TAG", "state: "+events.state)
        Log.e("TAG", "zipCode: "+events.zipCode)
        Log.e("TAG", "hostName: "+events.hostName)
        Log.e("TAG", "contactInfo: "+events.contactInfo)
        Log.e("TAG", "startDate "+events.startDate)
        Log.e("TAG", "endDate: "+events.endDate)
        Log.e("TAG", "startTime: "+events.startTime)
        Log.e("TAG", "endTime: "+events.endTime)

        when {
            events.eventName!!.isEmpty() -> {
                binding.inputEventName.requestFocus()
                binding.inputEventName.error = "Enter Event Name"
                return false
            }
            events.streetAddress!!.isEmpty() -> {
                binding.inputStreetAddress.requestFocus()
                binding.inputStreetAddress.error = "Enter Street Address"
                return false
            }
            events.city!!.isEmpty() -> {
                binding.inputCity.requestFocus()
                binding.inputCity.error = "Enter City Name"
                return false
            }
            events.state!!.isEmpty() -> {
                binding.inputState.requestFocus()
                binding.inputState.error = "Enter State Name"
                return false
            }
            events.zipCode!!.isEmpty() -> {
                binding.inputZipCode.requestFocus()
                binding.inputZipCode.error = "Enter Zip code"
                return false
            }
            events.hostName!!.isEmpty() -> {
                binding.inputHostName.requestFocus()
                binding.inputHostName.error = "Enter Host Name"
                return false
            }
            events.contactInfo!!.isEmpty() -> {
                binding.inputContactInfoOne.requestFocus()
                binding.inputContactInfoOne.error = "Enter Contact Info"
                return false
            }
            events.startTime!!.isEmpty() -> {
                binding.startTime.requestFocus()
                binding.startTime.error = "Enter Event Start Time"
                return false
            }
            events.endTime!!.isEmpty() -> {
                binding.endTime.requestFocus()
                binding.endTime.error = "Enter End Event Time"
                return false
            }
            events.startDate!!.isEmpty() -> {
                binding.inputStartDate.requestFocus()
                binding.inputStartDate.error = "Enter Event Start Date"
                return false
            }
            events.endDate!!.isEmpty() -> {
                binding.inputEndDate.requestFocus()
                binding.inputEndDate.error = "Enter Event End Date"
                return false
            }

            else -> return true
        }
    }
}