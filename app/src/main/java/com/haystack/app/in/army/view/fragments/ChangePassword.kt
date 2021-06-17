package com.haystack.app.`in`.army.view.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.haystack.app.`in`.army.databinding.FragmentChangePasswordBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.group_members.DefaultResponse
import com.haystack.app.`in`.army.utils.Extensions.showAlertDialog
import com.haystack.app.`in`.army.utils.Extensions.showSnackBar
import com.haystack.app.`in`.army.view.activity.MainMenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword: Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private var oldPassword: String? = null
    private var newPassword: String? = null
    private var confPassword: String? = null
    private var userId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarChangePassword.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnUpdate.setOnClickListener {
            oldPassword = binding.inputOldPassword.text.toString().trim()
            newPassword = binding.inputNewPassword.text.toString().trim()
            confPassword = binding.inputEditTextConfirmPassword.text.toString().trim()

            if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confPassword)){
                showSnackBar(binding.constraintChangePassword, "Please enter all fields")
                return@setOnClickListener
            }
            else if (newPassword != confPassword){
                showSnackBar(binding.constraintChangePassword, "Password does not match")
                return@setOnClickListener
            }

            updateNewPassword()
        }
    }

    private fun updateNewPassword() {
        userId = com.haystack.app.`in`.army.manager.SessionManager.instance.getUserId()
        Repository.changePassword(oldPassword!!, newPassword!!, userId!!)
            .enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){

                                binding.inputOldPassword.setText("")
                                binding.inputNewPassword.setText("")
                                binding.inputEditTextConfirmPassword.setText("")

                                showAlertDialog("Password Changed", requireContext(), response.body()?.message)

                            }else{

                                showAlertDialog("Failed!", requireContext(), response.body()?.message)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    showSnackBar(binding.constraintChangePassword, t.localizedMessage!!)
                }

            })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}