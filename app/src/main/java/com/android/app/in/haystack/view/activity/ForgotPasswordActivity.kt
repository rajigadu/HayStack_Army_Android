package com.android.app.`in`.haystack.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.ActivityForgotPasswordBinding
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.group_members.DefaultResponse
import com.android.app.`in`.haystack.utils.Extensions.showAlertDialog
import com.android.app.`in`.haystack.utils.Extensions.showSnackBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity: AppCompatActivity() {


    private lateinit var binding: ActivityForgotPasswordBinding
    private var email: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signIn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        binding.resetPassword.setOnClickListener {
            email = binding.inputEditTextEmail.text.toString().trim()
            if (TextUtils.isEmpty(email)){
                showSnackBar(binding.constraintForgotPassword, "Please enter registered email")
                return@setOnClickListener
            }
            sendRestPasswordLink()
        }
    }

    private fun sendRestPasswordLink() {
        Repository.forgotPassword(email!!).enqueue(object : Callback<DefaultResponse>{
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                try {

                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){
                            showSuccessAlert("Success", response.body()?.message!!)
                        }else{
                            showAlertDialog("Failed!", this@ForgotPasswordActivity, response.body()?.message)
                        }
                    }

                }catch (e: Exception){e.printStackTrace()}
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                showSnackBar(binding.constraintForgotPassword, t.localizedMessage!!)
            }

        })
    }

    private fun showSuccessAlert(title: String, message: String) {
        val dialog = MaterialAlertDialogBuilder(this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialogInterface, i ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .create()
        if (dialog.window != null)
            dialog.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation

        dialog.show()
    }

}
