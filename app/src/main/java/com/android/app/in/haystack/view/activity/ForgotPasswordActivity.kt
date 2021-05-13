package com.android.app.`in`.haystack.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.app.`in`.haystack.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity: AppCompatActivity() {


    private lateinit var binding: ActivityForgotPasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signIn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

    }

}
