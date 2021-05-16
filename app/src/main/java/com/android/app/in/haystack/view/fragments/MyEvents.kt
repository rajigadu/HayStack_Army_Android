package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.app.`in`.haystack.databinding.FragmentMyEventsBinding
import com.android.app.`in`.haystack.view.viewpager.EventsViewPagerAdapter
import com.android.app.`in`.haystack.view.viewpager.EventsViewPagerFragment
import com.google.android.material.tabs.TabLayoutMediator

class MyEvents: Fragment() {

    private lateinit var binding: FragmentMyEventsBinding
    private var tabTitles = arrayOf("My Events", "interests", "Attend", "Invited")
    private lateinit var viewPagerAdapter: EventsViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyEventsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = EventsViewPagerAdapter(requireActivity())
        viewPagerAdapter.addFragment(EventsViewPagerFragment())
        viewPagerAdapter.addFragment(EventsViewPagerFragment())
        viewPagerAdapter.addFragment(EventsViewPagerFragment())
        binding.myBookingViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.myEventsTabs, binding.myBookingViewPager) { tab, position ->
            tab.text = tabTitles[position]
            binding.myBookingViewPager.setCurrentItem(tab.position, true)
        }.attach()
    }
}