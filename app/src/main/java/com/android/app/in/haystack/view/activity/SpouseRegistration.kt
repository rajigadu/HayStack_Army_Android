package com.android.app.`in`.haystack.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.ActivitySpouseRegistrationBinding
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.soldier_signup.SignUpResponse
import com.android.app.`in`.haystack.utils.Extensions
import com.android.app.`in`.haystack.utils.Extensions.getDeviceUid
import com.android.app.`in`.haystack.utils.Extensions.showAlertDialog
import com.android.app.`in`.haystack.utils.Extensions.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SpouseRegistration: AppCompatActivity() {

    private lateinit var binding: ActivitySpouseRegistrationBinding
    private lateinit var bottomSheet: BottomSheetDialog

    private var fName: String? = null
    private var lName: String? = null
    private var relationToSm: String? = null
    private var email: String? = null
    private var sponsorsEmail: String? = null
    private var userName: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpouseRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.forgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.signin.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            if (validated()){
                if (password == confirmPassword){
                    if (email!!.contains("@") || sponsorsEmail!!.contains("@")){
                        completeSpouseRegistration()
                    }else{
                        showAlertDialog(
                            "Email not valid?", this,
                            "Please enter a valid email address"
                        )
                    }
                }else{
                    showAlertDialog(
                        "Password Not Match?", this,
                        "Password and confirm password does not match"
                    )
                }
            }
        }
    }

    private fun completeSpouseRegistration() {
        showBottomSheet()
        val deviceId = getDeviceUid(this)
        Repository.spouseRegistration(fName!!, lName!!, email!!, userName!!, password!!,
            sponsorsEmail!!, relationToSm!!, deviceId = deviceId).enqueue(
            object : Callback<SignUpResponse>{
                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){

                                showSuccessAlert("Registration Success", response.body()?.message!!)

                            }else{
                                showAlertDialog("Sign up Error?", this@SpouseRegistration, response.body()?.message)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                    hideBottomSheet()
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    Extensions.showErrorResponse(t, binding.constraintSpouseSignUp)
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
        email = binding.inputEmailAddress.text.toString().trim()
        relationToSm = binding.relationshipToSm.text.toString().trim()
        sponsorsEmail = binding.inputSponsorsGovtEmail.text.toString().trim()
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
            relationToSm!!.isEmpty() -> {
                binding.relationshipToSm.requestFocus()
                binding.relationshipToSm.error = "Enter Relation to sm"
                return false
            }
            sponsorsEmail!!.isEmpty() -> {
                binding.inputSponsorsGovtEmail.requestFocus()
                binding.inputSponsorsGovtEmail.error = "Enter sponsors govt email"
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

    @SuppressLint("SetTextI18n")
    private fun showBottomSheet(){
        bottomSheet = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(applicationContext)
            .inflate(
                R.layout.authentication_progress_bottom_sheet,
                findViewById<ConstraintLayout>(R.id.bottom_sheet)
            )
        val title = view.findViewById<TextView>(R.id.progress_title)
        val subTitle = view.findViewById<TextView>(R.id.progress_sub_title)

        title.text = "Spouse Registration"
        subTitle.text = "Verifying Registration Details, Please wait..."

        bottomSheet.setCancelable(false)
        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private  fun hideBottomSheet(){
        bottomSheet.hide()
    }
}