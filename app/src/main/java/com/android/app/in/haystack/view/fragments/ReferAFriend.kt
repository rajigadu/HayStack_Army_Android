package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.app.`in`.haystack.databinding.FragmentReferFriendBinding
import com.android.app.`in`.haystack.view.activity.MainMenuActivity

class ReferAFriend: Fragment() {

    private lateinit var binding: FragmentReferFriendBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReferFriendBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).updateBottomNavChange(3)
    }
}