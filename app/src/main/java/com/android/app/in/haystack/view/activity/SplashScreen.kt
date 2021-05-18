 package com.android.app.`in`.haystack.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.manager.SessionManager

 class SplashScreen : AppCompatActivity() {

     private val splash_delay: Long = 3000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        initView()
    }

     private fun initView() {
         SessionManager.init(this)
         Handler(Looper.getMainLooper()).postDelayed(mRunnable, splash_delay)
     }

     private val mRunnable = Runnable {
         if (!isFinishing){
             if (SessionManager.instance.getUserId().isNotEmpty()){

                 startActivity(Intent(this, MainMenuActivity::class.java))
                 finish()

             }else {
                 startActivity(Intent(this, LogInActivity::class.java))
                 finish()
             }
         }
     }
 }