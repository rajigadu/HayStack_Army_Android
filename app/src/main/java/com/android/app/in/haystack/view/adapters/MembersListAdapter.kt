package com.android.app.`in`.haystack.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutMembersListItemViewBinding


class MembersListAdapter(val context: Context)
    : RecyclerView.Adapter<MembersListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: LayoutMembersListItemViewBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersListAdapter.ViewHolder =
        ViewHolder(LayoutMembersListItemViewBinding.inflate(LayoutInflater.from(
            parent.context
        ), parent, false))

    override fun onBindViewHolder(holder: MembersListAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 20


}