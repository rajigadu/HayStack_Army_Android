package com.haystack.app.`in`.army.view.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.haystack.app.`in`.army.databinding.FragmentEditProfileBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.group_members.DefaultResponse
import com.haystack.app.`in`.army.utils.Extensions.showAlertDialog
import com.haystack.app.`in`.army.utils.Extensions.showSnackBar
import com.haystack.app.`in`.army.view.activity.MainMenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfile: Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private var firstName: String? = null
    private var lastName: String? = null
    private var userName: String? = null
    private var logniedUser: String? = null
    private var userId: String? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarEditProfile.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnUpdate.setOnClickListener {
            firstName = binding.inputEditTextFirstName.text.toString().trim()
            lastName = binding.inputEditTextLastName.text.toString().trim()
            userName = binding.inputEditTextUsername.text.toString().trim()

            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(userName)){
                showSnackBar(binding.constraintEditProfile, "Please fill all fields")
                return@setOnClickListener
            }

            updateEditProfile()
        }
    }

    private fun updateEditProfile() {
        userId = com.haystack.app.`in`.army.manager.SessionManager.instance.getUserId()
        logniedUser = com.haystack.app.`in`.army.manager.SessionManager.instance.getLoginUser()

        Repository.editProfile(firstName!!, lastName!!, userName!!, logniedUser!!, userId!!)
            .enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){
                                binding.inputEditTextFirstName.setText("")
                                binding.inputEditTextLastName.setText("")
                                binding.inputEditTextUsername.setText("")
                                showAlertDialog("Success", requireContext(), response.body()?.message)

                            }else{
                                showAlertDialog("Failed!", requireContext(), response.body()?.message)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    showSnackBar(binding.constraintEditProfile, t.localizedMessage!!)
                }

            })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}