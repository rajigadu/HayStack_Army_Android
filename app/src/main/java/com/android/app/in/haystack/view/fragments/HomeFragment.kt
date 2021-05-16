package com.android.app.`in`.haystack.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentHomeBinding
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.btnMyEvents.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_myEvents)
        }
    }


    @SuppressLint("SimpleDateFormat")
    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).updateBottomNavChange(0)
        (activity as MainMenuActivity).showBottomNav()
        val sdf = SimpleDateFormat("EEEE,dd MMM")
        binding.currentDate.text = sdf.format(Date())
    }
}