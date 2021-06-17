package com.haystack.app.`in`.army.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.haystack.app.`in`.army.databinding.FragmentEventsBinding
import com.haystack.app.`in`.army.utils.AppConstants.ARG_OBJECTS
import com.haystack.app.`in`.army.view.activity.MainMenuActivity
import com.haystack.app.`in`.army.view.viewpager.*

class EventsViewpagerFragment: Fragment() {

    private lateinit var binding: FragmentEventsBinding
    private var tabTitles = arrayOf("My Events", "Interests", "Attend", "Invited")
    private lateinit var viewPagerAdapter: EventsViewPagerAdapter

    private var currentPosition: Int? = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentPosition = arguments?.getInt(ARG_OBJECTS)
        Log.e("TAG", "currentPosition: $currentPosition")

        viewPagerAdapter = EventsViewPagerAdapter(requireActivity())
        viewPagerAdapter.addFragment(MyEventsFragment())
        viewPagerAdapter.addFragment(InterestsEventsFragment())
        viewPagerAdapter.addFragment(AttendEventsFragment())
        viewPagerAdapter.addFragment(InvitedEventsFragment())

        binding.myBookingViewPager.adapter = viewPagerAdapter
        binding.myBookingViewPager.post {
            binding.myBookingViewPager.setCurrentItem(currentPosition!!, true)
        }

        TabLayoutMediator(binding.myEventsTabs, binding.myBookingViewPager) { tab, position ->
            tab.text = tabTitles[position]
            binding.myBookingViewPager.setCurrentItem(tab.position, true)
        }.attach()

        binding.toolbarMyEvents.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}