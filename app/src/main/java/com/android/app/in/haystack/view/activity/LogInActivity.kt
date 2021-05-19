package com.android.app.`in`.haystack.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.ActivityLoginBinding
import com.android.app.`in`.haystack.manager.SessionManager
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.login.LogIn
import com.android.app.`in`.haystack.utils.Extensions.getDeviceUid
import com.android.app.`in`.haystack.utils.Extensions.shortSnackBar
import com.android.app.`in`.haystack.utils.Extensions.showAlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var bottomSheet: BottomSheetDialog
    var userName: String? = null
    var password: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.signup.setOnClickListener {
            startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
            finish()
        }

        binding.forgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }

        binding.signIn.setOnClickListener {
            userName = binding.inputEditTextEmail.text.toString().trim()
            password = binding.inputEditTextPassword.text.toString().trim()

            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                showAlertDialog("Enter Valid Credential?",this, "Please enter a valid username and password")
                return@setOnClickListener
            }

            validateUserCredentials()
        }
    }

    private fun validateUserCredentials() {
        val deviceId = getDeviceUid(this)
        showBottomSheet()
        Repository.userLogIn(userName!!, password!!, deviceId).enqueue(
            object : Callback<LogIn>{
                override fun onResponse(call: Call<LogIn>, response: Response<LogIn>) {
                    Log.e("TAG","response: "+response.body())
                    try {

                        if (response.isSuccessful){
                            if (response.body()!!.status == "1"){

                                SessionManager.instance.saveUserCredentials(response.body()!!)
                                startActivity(Intent(this@LogInActivity, MainMenuActivity::class.java))
                                finish()

                            }else{
                                showAlertDialog("LogIn Error?",this@LogInActivity, response.body()!!.message)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}

                    hideBottomSheet()
                }

                override fun onFailure(call: Call<LogIn>, t: Throwable) {
                    shortSnackBar(t.message!!, binding.constraintLogin)
                    hideBottomSheet()
                }

            })
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