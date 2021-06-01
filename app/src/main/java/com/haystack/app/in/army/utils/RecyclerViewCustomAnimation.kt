package com.haystack.app.`in`.army.utils

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.haystack.app.`in`.army.R

class RecyclerViewCustomAnimation: DefaultItemAnimator() {


    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {

        holder?.itemView?.animation = AnimationUtils.loadAnimation(
            holder?.itemView?.context,
            R.anim.anim_bottom_to_top
        )

        return super.animateAdd(holder)
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        return super.animateRemove(holder)
    }


}