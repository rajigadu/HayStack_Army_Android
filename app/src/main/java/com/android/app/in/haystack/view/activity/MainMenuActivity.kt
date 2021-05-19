package com.android.app.`in`.haystack.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.ActivityMainMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainMenuActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var navController: NavHostController
    private var selectedId: Int? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController() as NavHostController
        navController.navigateUp()

        binding.navHome.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }

        binding.navProfile.setOnClickListener {
            navController.navigate(R.id.profile)
        }

        binding.navRefer.setOnClickListener {
            navController.navigate(R.id.referAFriend)
        }

        binding.createEvent.setOnClickListener {
            navController.navigate(R.id.createEvent)
        }

        binding.navEvents.setOnClickListener {
            navController.navigate(R.id.groupsFragment)
        }

    }

    fun updateBottomNavChange(position: Int){
        when(position){
            0 -> {
                binding.navHome.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                binding.navProfile.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navRefer.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navEvents.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.createEvent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
            }
            1 -> {
                binding.navHome.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navProfile.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navRefer.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navEvents.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                binding.createEvent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
            }
            2 -> {
                binding.navHome.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navProfile.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                binding.navRefer.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navEvents.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.createEvent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
            }
            3 -> {
                binding.navHome.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navProfile.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navRefer.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
                binding.navEvents.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.createEvent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
            }
            4 -> {
                binding.navHome.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navProfile.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navRefer.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.navEvents.setColorFilter(ContextCompat.getColor(this, R.color.colorDefaultBottomNavIcon))
                binding.createEvent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary))
            }
        }
    }

    fun hideBottomNav(){
        binding.customBottomNavigation.visibility = GONE
    }

    fun showBottomNav(){
        binding.customBottomNavigation.visibility = VISIBLE
    }

}
