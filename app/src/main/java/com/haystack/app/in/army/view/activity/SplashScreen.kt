 package com.haystack.app.`in`.army.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.haystack.app.`in`.army.R

 private const val SPLASH_DELAY: Long = 3000

 class SplashScreen : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        initView()
    }

     private fun initView() {
         com.haystack.app.`in`.army.manager.SessionManager.init(this)
         Handler(Looper.getMainLooper()).postDelayed(mRunnable, SPLASH_DELAY)
     }

     private val mRunnable = Runnable {
         if (!isFinishing){
             if (com.haystack.app.`in`.army.manager.SessionManager.instance.getUserId().isNotEmpty()){

                 startActivity(Intent(this, MainMenuActivity::class.java))
                 finish()

             }else {
                 startActivity(Intent(this, LogInActivity::class.java))
                 finish()
             }
         }
     }
 }