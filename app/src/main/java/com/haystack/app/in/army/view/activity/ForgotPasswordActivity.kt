package com.haystack.app.`in`.army.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.haystack.app.`in`.army.R
import com.haystack.app.`in`.army.databinding.ActivityForgotPasswordBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.group_members.DefaultResponse
import com.haystack.app.`in`.army.utils.Extensions
import com.haystack.app.`in`.army.utils.Extensions.showAlertDialog
import com.haystack.app.`in`.army.utils.Extensions.showSnackBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity: AppCompatActivity() {


    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var bottomSheet: BottomSheetDialog
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
        showBottomSheet()
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
                hideBottomSheet()
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Extensions.showErrorResponse(t, binding.constraintForgotPassword)
                hideBottomSheet()
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

    private fun showBottomSheet(){
        bottomSheet = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(applicationContext)
            .inflate(
                R.layout.authentication_progress_bottom_sheet,
                findViewById<ConstraintLayout>(R.id.bottom_sheet)
            )
        bottomSheet.setCancelable(false)
        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private  fun hideBottomSheet(){
        bottomSheet.hide()
    }

}
