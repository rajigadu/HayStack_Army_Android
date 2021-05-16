package com.android.app.`in`.haystack.view.viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentViewpagerEventsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsViewPagerFragment: Fragment() {


    private lateinit var binding: FragmentViewpagerEventsBinding
    private lateinit var eventsAdapter: EventsRecyclerViewAdapter
    //private var listAllBookings = arrayListOf<Bookings>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewpagerEventsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eventsRefresher.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))


        eventsAdapter = EventsRecyclerViewAdapter(requireContext())
        binding.recyclerEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventsAdapter
        }

        binding.eventsRefresher.setOnRefreshListener {

        }
    }

}