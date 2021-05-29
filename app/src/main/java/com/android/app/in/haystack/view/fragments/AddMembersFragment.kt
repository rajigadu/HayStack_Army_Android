package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentAddMemberBinding
import com.android.app.`in`.haystack.network.response.event.AllMembers
import com.android.app.`in`.haystack.network.response.event.Event
import com.android.app.`in`.haystack.utils.AppConstants.ARG_SERIALIZABLE
import com.android.app.`in`.haystack.utils.Extensions.showSnackBar
import com.android.app.`in`.haystack.view.activity.MainMenuActivity

class AddMembersFragment: Fragment() {

    private lateinit var binding: FragmentAddMemberBinding
    private var events: Event? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMemberBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events = arguments?.getSerializable(ARG_SERIALIZABLE) as Event
        Log.e("TAG", "events: $events")

        binding.toolbarAddMember.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnInvite.setOnClickListener {
            val fullName = binding.inputName.text.toString().trim()
            val email = binding.inputEmail.text.toString().trim()
            val mobile = binding.inputMobile.text.toString().trim()

            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(mobile)){
                showSnackBar(binding.constraintAddMember, "Please fill all the fields")
                return@setOnClickListener
            }
            //events?.allmembers?.clear()
            events?.allmembers?.add(AllMembers(fullName, email, mobile))

            val bundle = bundleOf(ARG_SERIALIZABLE to events)
            findNavController().navigate(R.id.action_addMembersFragment_to_membersPublish, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}