package com.android.app.`in`.haystack.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentProfileBinding
import com.android.app.`in`.haystack.view.activity.LogInActivity
import com.android.app.`in`.haystack.view.activity.MainMenuActivity

class Profile: Fragment() {


    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutLogout.setOnClickListener {
            startActivity(Intent(requireContext(), LogInActivity::class.java))
            requireActivity().finish()
        }

        binding.layoutChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_changePassword)
        }

        binding.layoutContactUs.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_contactUs)
        }

        binding.layoutEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_editProfile)
        }

    }


    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).updateBottomNavChange(2)
    }
}