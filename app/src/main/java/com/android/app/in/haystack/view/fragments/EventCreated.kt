package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentEventCreatedBinding
import com.android.app.`in`.haystack.view.activity.MainMenuActivity

class EventCreated: Fragment() {

    private lateinit var binding: FragmentEventCreatedBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventCreatedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarEventCreated.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnClose.setOnClickListener {
            findNavController().navigate(R.id.action_eventCreated_to_homeFragment)
        }

        binding.btnCrateAnotherEvent.setOnClickListener {
            findNavController().navigate(R.id.action_eventCreated_to_createEvent)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}