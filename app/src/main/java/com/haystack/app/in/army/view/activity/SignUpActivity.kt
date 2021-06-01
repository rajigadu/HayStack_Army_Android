package com.haystack.app.`in`.army.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haystack.app.`in`.army.databinding.ActivitySignupBinding

class SignUpActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnSoldierRegistration.setOnClickListener {
            startActivity(Intent(this, SoldierRegistration::class.java))
        }

        binding.btnSpouseRegistration.setOnClickListener {
            startActivity(Intent(this, SpouseRegistration::class.java))
        }
    }
}