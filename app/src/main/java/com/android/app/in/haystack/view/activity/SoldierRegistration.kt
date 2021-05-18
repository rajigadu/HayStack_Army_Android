package com.android.app.`in`.haystack.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent.ACTION_UP
import androidx.appcompat.app.AppCompatActivity
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.ActivitySoldierRegistrationBinding
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.soldier_signup.SignUpResponse
import com.android.app.`in`.haystack.utils.Extensions.getDeviceUid
import com.android.app.`in`.haystack.utils.Extensions.hideKeyboard
import com.android.app.`in`.haystack.utils.Extensions.showAlertDialog
import com.android.app.`in`.haystack.utils.Extensions.showSnackBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SoldierRegistration: AppCompatActivity() {

    private lateinit var binding: ActivitySoldierRegistrationBinding
    private var fName: String? = null
    private var lName: String? = null
    private var dodId: String? = null
    private var govtEmail: String? = null
    private var userName: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldierRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signin.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        binding.forgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        
        binding.constraintSingUpSoldier.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                ACTION_UP ->{
                    binding.constraintSingUpSoldier.hideKeyboard()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener  false
        }

        binding.btnSignUp.setOnClickListener {
            if (validated()){
                if (password == confirmPassword){
                    if (govtEmail!!.contains("@")){
                        completeSoldierRegistration()
                    }else{
                        showAlertDialog(
                            "Email not valid?", this,
                            "Please enter a valid email address"
                        )
                    }
                }else{
                    showAlertDialog("Password Not Match?", this,
                        "Password and confirm password does not match")
                }
            }
        }
    }

    private fun completeSoldierRegistration() {
        val deviceId = getDeviceUid(this)
        Repository.soldierRegistration(fName!!, lName!!, govtEmail!!, userName!!, password!!, dodId!!, deviceId = deviceId)
            .enqueue(object : Callback<SignUpResponse>{
                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){

                                showSuccessAlert("Registration Success", response.body()?.message!!)

                            }else{
                                showAlertDialog("Sign up Error?", this@SoldierRegistration, response.body()?.message)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    showSnackBar(binding.constraintSingUpSoldier, t.localizedMessage)
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
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            }
            .create()
        if (dialog.window != null)
            dialog.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation

        dialog.show()
    }

    private fun validated(): Boolean {
        fName = binding.inputFirstName.text.toString().trim()
        lName = binding.inputLastName.text.toString().trim()
        dodId = binding.inputDodId.text.toString().trim()
        govtEmail = binding.inputGovtEmail.text.toString().trim()
        userName = binding.inputUsername.text.toString().trim()
        password = binding.inputPassword.text.toString().trim()
        confirmPassword = binding.inputConfirmPassword.text.toString().trim()

        when {
            fName!!.isEmpty() -> {
                binding.inputFirstName.requestFocus()
                binding.inputFirstName.error = "Enter First Name"
                return false
            }
            lName!!.isEmpty() -> {
                binding.inputLastName.requestFocus()
                binding.inputLastName.error = "Enter Last Name"
                return false
            }
            dodId!!.isEmpty() -> {
                binding.inputDodId.requestFocus()
                binding.inputDodId.error = "Enter DOD Id"
                return false
            }
            govtEmail!!.isEmpty() -> {
                binding.inputGovtEmail.requestFocus()
                binding.inputGovtEmail.error = "Enter govt email address"
                return false
            }
            userName!!.isEmpty() -> {
                binding.inputUsername.requestFocus()
                binding.inputUsername.error = "Enter User Name"
                return false
            }
            password!!.isEmpty() -> {
                binding.inputPassword.requestFocus()
                binding.inputPassword.error = "Enter Password"
                return false
            }
            confirmPassword!!.isEmpty() -> {
                binding.inputConfirmPassword.requestFocus()
                binding.inputConfirmPassword.error = "Enter Confirm Password"
                return false
            }
            else -> return true
        }
    }

}