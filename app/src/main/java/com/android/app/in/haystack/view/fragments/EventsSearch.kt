package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentEventSearchBinding
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.android.app.`in`.haystack.view.adapters.EventSearchListAdapter

class EventsSearch: Fragment(), EventSearchListAdapter.EventSearchListItemClick {

    private lateinit var binding: FragmentEventSearchBinding
    private lateinit var eventSearchListAdapter: EventSearchListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventSearchBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarEventsSearch.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        eventSearchListAdapter = EventSearchListAdapter(requireContext(), this)
        binding.eventsSearchListView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventSearchListAdapter
        }
    }

    override fun eventListItemClick() {
        findNavController().navigate(R.id.action_eventsSearch_to_eventsInfoFragment)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}