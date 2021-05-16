package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentEventInfoBinding
import com.android.app.`in`.haystack.view.activity.MainMenuActivity

class EventsInfoFragment: Fragment() {

    private lateinit var binding: FragmentEventInfoBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventInfoBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarEventInfo.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnNotInterested.setOnClickListener {
         findNavController().navigate(R.id.action_eventsInfoFragment_to_homeFragment)
        }

        binding.btnInterested.setOnClickListener {
            findNavController().navigate(R.id.action_eventsInfoFragment_to_myEvents)
        }

        binding.btnAttend.setOnClickListener {
            findNavController().navigate(R.id.action_eventsInfoFragment_to_myEvents)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}