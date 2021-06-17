package com.haystack.app.`in`.army.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.haystack.app.`in`.army.R
import com.haystack.app.`in`.army.databinding.FragmentProfileBinding
import com.haystack.app.`in`.army.view.activity.LogInActivity
import com.haystack.app.`in`.army.view.activity.MainMenuActivity

class Profile: Fragment() {


    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutLogout.setOnClickListener {
            showConfirmationDialog()
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

        binding.layoutTermsAndConditions.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_termsAndConditions)
        }

    }

    private fun showConfirmationDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle("Logout")
            .setMessage(" Are you sure want to logout.?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialogInterface, i ->
                dialogInterface.dismiss()
                com.haystack.app.`in`.army.manager.SessionManager.instance.clearSessionData()
                startActivity(Intent(requireContext(), LogInActivity::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("No"){ dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()
        if (dialog.window != null)
            dialog.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation

        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).updateBottomNavChange(2)
        (activity as MainMenuActivity).showBottomNav()
    }
}