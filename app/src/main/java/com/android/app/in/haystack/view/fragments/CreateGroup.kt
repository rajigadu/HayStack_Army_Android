package com.android.app.`in`.haystack.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentCreateGroupBinding
import com.android.app.`in`.haystack.manager.SessionManager
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.create_group.Group
import com.android.app.`in`.haystack.network.response.group_members.DefaultResponse
import com.android.app.`in`.haystack.utils.Extensions.hideKeyboard
import com.android.app.`in`.haystack.utils.Extensions.showAlertDialog
import com.android.app.`in`.haystack.utils.Extensions.showSnackBar
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGroup: Fragment() {

    private lateinit var binding: FragmentCreateGroupBinding

    private var groupName: String? = null
    private var groupDesc: String? = null
    private var memberName: String? = null
    private var memberEmail: String? = null
    private var memberMobile: String? = null
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateGroupBinding.inflate(layoutInflater)
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarCreateGroup.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.constraintCreateGroup.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                KeyEvent.ACTION_UP ->{
                    binding.constraintCreateGroup.hideKeyboard()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener  false
        }

        binding.btnCreateNewGroup.setOnClickListener {
            if (validated()){
                addNewGroup()
            }
        }
    }

    private fun addNewGroup() {
        userId = SessionManager.instance.getUserId()
        Repository.createNewGroup(groupName!!, groupDesc!!, userId!!).enqueue(object : Callback<Group>{
            override fun onResponse(call: Call<Group>, response: Response<Group>) {

                try {

                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){

                            addMember(response.body()?.groupid.toString())

                        }else{
                            showAlertDialog("Failed", requireContext(), response.body()?.message)
                        }
                    }

                }catch (e: Exception){e.printStackTrace()}
            }

            override fun onFailure(call: Call<Group>, t: Throwable) {
                showSnackBar(binding.constraintCreateGroup, t.localizedMessage!!)
            }

        })
    }

    private fun addMember(groupid: String?) {
        Repository.addMemberToGroup(groupid!!, userId!!, memberName!!, memberMobile!!, memberEmail!!)
            .enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){

                            showSuccessAlert("Group Created", response.body()?.message!!)

                        }else{
                            showAlertDialog("Failed", requireContext(), response.body()?.message)
                        }
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    showSnackBar(binding.constraintCreateGroup, t.localizedMessage!!)
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

    private fun validated(): Boolean {
        groupName = binding.inputGroupName.text.toString().trim()
        groupDesc = binding.inputGroupDesc.text.toString().trim()
        memberName = binding.inputName.text.toString().trim()
        memberEmail = binding.inputEmail.text.toString().trim()
        memberMobile = binding.inputMobile.text.toString().trim()

        when {
            groupName!!.isEmpty() -> {
                binding.inputGroupName.requestFocus()
                binding.inputGroupName.error = "Enter Group Name"
                return false
            }
            groupDesc!!.isEmpty() -> {
                binding.inputGroupDesc.requestFocus()
                binding.inputGroupDesc.error = "Enter Group Description"
                return false
            }
            memberName!!.isEmpty() -> {
                binding.inputName.requestFocus()
                binding.inputName.error = "Enter Member Name"
                return false
            }
            memberEmail!!.isEmpty() -> {
                binding.inputEmail.requestFocus()
                binding.inputEmail.error = "Enter Member Email"
                return false
            }
            memberMobile!!.isEmpty() -> {
                binding.inputMobile.requestFocus()
                binding.inputMobile.error = "Enter Member Mobile"
                return false
            }
            else -> return true
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}