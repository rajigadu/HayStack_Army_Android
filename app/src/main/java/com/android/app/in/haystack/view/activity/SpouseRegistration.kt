package com.android.app.`in`.haystack.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.app.`in`.haystack.databinding.ActivitySpouseRegistrationBinding

class SpouseRegistration: AppCompatActivity() {

    private lateinit var binding: ActivitySpouseRegistrationBinding

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
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}