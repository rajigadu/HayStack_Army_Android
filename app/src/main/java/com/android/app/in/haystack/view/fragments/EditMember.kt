package com.android.app.`in`.haystack.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent.ACTION_UP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentEditMemberBinding
import com.android.app.`in`.haystack.manager.SessionManager
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.event.Event
import com.android.app.`in`.haystack.network.response.group_members.DefaultResponse
import com.android.app.`in`.haystack.utils.AppConstants.ARG_SERIALIZABLE
import com.android.app.`in`.haystack.utils.AppConstants.MEMBER_EMAIL
import com.android.app.`in`.haystack.utils.AppConstants.MEMBER_NAME
import com.android.app.`in`.haystack.utils.AppConstants.MEMBER_PHONE
import com.android.app.`in`.haystack.utils.AppConstants.POSITION
import com.android.app.`in`.haystack.utils.AppConstants.STATUS
import com.android.app.`in`.haystack.utils.Extensions
import com.android.app.`in`.haystack.utils.Extensions.hideKeyboard
import com.android.app.`in`.haystack.utils.Extensions.showSnackBar
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMember: Fragment() {

    private lateinit var binding: FragmentEditMemberBinding
    private var status: String? = null
    private var groupId: String? = null
    private var name: String? = null
    private var email: String? = null
    private var phone: String? = null
    private var events: Event? = null
    private var position: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditMemberBinding.inflate(layoutInflater)
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        status =  arguments?.getString(STATUS)
        if (status == "1"){
            name = arguments?.getString(MEMBER_NAME)
            email = arguments?.getString(MEMBER_EMAIL)
            phone = arguments?.getString(MEMBER_PHONE)

            binding.inputName.setText(name)
            binding.inputEmail.setText(email)
            binding.inputMobile.setText(phone)
        }else if (status == "2"){
            events = arguments?.getSerializable(ARG_SERIALIZABLE) as Event
            position = arguments?.getInt(POSITION)

            binding.inputName.setText(events?.allmembers!![position!!].member)
            binding.inputEmail.setText(events?.allmembers!![position!!].email)
            binding.inputMobile.setText(events?.allmembers!![position!!].number)
        }

        binding.toolbarEditMember.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.constraintEditMember.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                ACTION_UP -> {
                    binding.constraintEditMember.hideKeyboard()
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }

        binding.btnUpdate.setOnClickListener {
            name = binding.inputName.text.toString().trim()
            email = binding.inputEmail.text.toString().trim()
            phone = binding.inputMobile.text.toString().trim()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)){
                showSnackBar(binding.constraintEditMember, "Please enter all the fields")
                return@setOnClickListener
            }

            when (status) {
                "1" -> updateMemberDetails()
                "2" -> updateMemberList()
                else -> addNewMember()
            }
        }
    }

    private fun updateMemberList() {
        events?.allmembers!![position!!].number = phone!!
        events?.allmembers!![position!!].email = email!!
        events?.allmembers!![position!!].member = name!!
    }

    private fun addNewMember() {
        val userId = SessionManager.instance.getUserId()
        Repository.addMemberToGroup(groupId!!, userId, name!!, phone!!, email!!)
            .enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){

                            showSuccessAlert("Member Added", response.body()?.message!!)

                        }else{
                            Extensions.showAlertDialog(
                                "Failed",
                                requireContext(),
                                response.body()?.message
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    showSnackBar(binding.constraintEditMember, t.localizedMessage!!)
                }

            })
    }

    private fun updateMemberDetails() {
        val userId = SessionManager.instance.getUserId()
        Repository.editGroupMember(groupId!!, name!!, phone!!, email!!, userId)
            .enqueue(object: Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){

                            showSuccessAlert("Member Updated", response.body()?.message!!)

                        }else{
                            Extensions.showAlertDialog(
                                "Failed",
                                requireContext(),
                                response.body()?.message
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    showSnackBar(binding.constraintEditMember, t.localizedMessage!!)
                }
            })
    }

    private fun showSuccessAlert(title: String, message: String) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialogInterface, i ->
                dialogInterface.dismiss()
                findNavController().popBackStack()
            }
            .create()
        if (dialog.window != null)
            dialog.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}