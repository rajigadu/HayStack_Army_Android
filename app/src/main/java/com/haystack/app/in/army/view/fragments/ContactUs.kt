package com.haystack.app.`in`.army.view.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.haystack.app.`in`.army.databinding.FragmentContactUsBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.group_members.DefaultResponse
import com.haystack.app.`in`.army.utils.Extensions.showAlertDialog
import com.haystack.app.`in`.army.utils.Extensions.showSnackBar
import com.haystack.app.`in`.army.view.activity.MainMenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUs: Fragment() {


    private lateinit var binding: FragmentContactUsBinding

    private var fullName: String? = null
    private var nameOrEmail: String? = null
    private var description: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactUsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarContactUs.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnUpdate.setOnClickListener {
            fullName = binding.inputEditTextFullName.text.toString().trim()
            nameOrEmail = binding.inputEditTextEmail.text.toString().trim()
            description = binding.inputDesc.text.toString().trim()

            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(nameOrEmail) || TextUtils.isEmpty(description)){
                showSnackBar(binding.constraintContactUs, "Please enter all fields")
                return@setOnClickListener
            }

            contactUs()
        }

    }

    private fun contactUs(){
        Repository.contactUs(fullName!!, nameOrEmail!!, description!!).enqueue(object :
        Callback<DefaultResponse>{
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                try {

                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){

                            showAlertDialog("Success", requireContext(), response.body()?.message)
                            binding.inputEditTextEmail.setText("")
                            binding.inputEditTextFullName.setText("")
                            binding.inputDesc.setText("")

                        }else{
                            showAlertDialog("Failed!", requireContext(), response.body()?.message)
                        }
                    }

                }catch (e: Exception){e.printStackTrace()}
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                showSnackBar(binding.constraintContactUs, t.localizedMessage!!)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}