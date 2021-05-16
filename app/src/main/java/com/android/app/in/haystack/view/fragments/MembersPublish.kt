package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentMembersPublishBinding
import com.android.app.`in`.haystack.view.activity.MainMenuActivity

class MembersPublish: Fragment() {


    private lateinit var binding: FragmentMembersPublishBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMembersPublishBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarMembersPublish.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnPublish.setOnClickListener {
            findNavController().navigate(R.id.action_membersPublish_to_eventCreated)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}