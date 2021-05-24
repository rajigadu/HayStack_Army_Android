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
import com.android.app.`in`.haystack.databinding.FragmentCreateEventModeBinding
import com.android.app.`in`.haystack.network.response.event.Event
import com.android.app.`in`.haystack.utils.AppConstants.ARG_SERIALIZABLE
import com.android.app.`in`.haystack.view.activity.MainMenuActivity

class CreateEventMode: Fragment() {


    private lateinit var binding: FragmentCreateEventModeBinding
    private var advertiseEvent: String = "Public"
    private var hostContactInfo: String = "Public"
    private var events: Event? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateEventModeBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events = arguments?.getSerializable(ARG_SERIALIZABLE) as Event

        binding.toolbarEventMode.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnContinue.setOnClickListener {

            val checkedAdvertiseEventId = binding.advertiseEventGroup.checkedRadioButtonId
            findAdvertiseEvent(checkedAdvertiseEventId)
            val checkedHostContactInfoId = binding.hostContactInfoGroup.checkedRadioButtonId
            findHostContactInfo(checkedHostContactInfoId)

            if (advertiseEvent.isNullOrEmpty() || hostContactInfo.isNullOrEmpty()){
                return@setOnClickListener
            }
            events?.eventType = advertiseEvent
            events?.hostType = hostContactInfo
            Log.e("TAG", "events: $events")
            val bundle = bundleOf(ARG_SERIALIZABLE to events)
            findNavController().navigate(R.id.action_createEventMode_to_addMembersFragment, bundle)
        }

        binding.toolbarEventMode.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun findAdvertiseEvent(checkedId: Int) {
        when(checkedId){
            R.id.advertisePublic -> {
                advertiseEvent = "Public"
            }
            R.id.advertisePrivate -> {
                advertiseEvent = "Private"
            }
        }
    }

    private fun findHostContactInfo(checkedId: Int) {
        when(checkedId){
            R.id.hostContactPublic -> {
                hostContactInfo = "Public"
            }
            R.id.hostContactPrivate -> {
                hostContactInfo = "Private"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}