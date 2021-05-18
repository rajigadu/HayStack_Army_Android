package com.android.app.`in`.haystack.view.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentEditGroupBinding
import com.android.app.`in`.haystack.manager.SessionManager
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.group_members.DefaultResponse
import com.android.app.`in`.haystack.utils.AppConstants.GROUP_ID
import com.android.app.`in`.haystack.utils.Extensions
import com.android.app.`in`.haystack.utils.Extensions.showSnackBar
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditGroup: Fragment() {

    private lateinit var binding: FragmentEditGroupBinding
    
    private var groupName: String? = null
    private var groupDesc: String? = null
    private var groupId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditGroupBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupId = arguments?.getString(GROUP_ID)

        binding.toolbarEditGroup.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnUpdateEditGroup.setOnClickListener {
            groupName = binding.inputGroupName.text.toString().trim()
            groupDesc = binding.inputGroupDesc.text.toString().trim()

            if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(groupDesc)){
                showSnackBar(binding.constraintEditGroup, "Please enter all fields")
                return@setOnClickListener
            }

            updateEditedGroup()
        }
    }

    private fun updateEditedGroup() {
        val userId = SessionManager.instance.getUserId()
        Repository.editGroup(groupName!!, groupDesc!!, groupId!!, userId)
            .enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    try {

                        if (response.isSuccessful){
                            if (response.body()!!.status == "1"){

                                showSuccessAlert("Updated Successfully", response.body()?.message!!)

                            }else{
                                Extensions.showAlertDialog(
                                    "Some Error Occurred",
                                    requireContext(),
                                    response.body()!!.message
                                )
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    showSnackBar(binding.constraintEditGroup, t.localizedMessage!!)
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