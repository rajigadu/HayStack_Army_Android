package com.android.app.`in`.haystack.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.app.`in`.haystack.databinding.ActivityMainMenuBinding

class MainMenuActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}
