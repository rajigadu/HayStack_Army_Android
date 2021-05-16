package com.android.app.`in`.haystack.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.app.`in`.haystack.databinding.ActivitySoldierRegistrationBinding

class SoldierRegistration: AppCompatActivity() {

    private lateinit var binding: ActivitySoldierRegistrationBinding


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

    }
}