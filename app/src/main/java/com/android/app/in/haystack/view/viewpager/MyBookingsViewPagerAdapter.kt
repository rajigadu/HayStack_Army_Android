package com.android.app.`in`.deltaservices.view.adpters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyBookingsViewPagerAdapter(f: FragmentActivity): FragmentStateAdapter(f) {

    private val listFragments: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = listFragments.size

    override fun createFragment(position: Int): Fragment = listFragments[position]


    fun addFragment(fragment: Fragment?) {
        listFragments.add(fragment!!)
    }

}