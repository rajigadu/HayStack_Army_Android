package com.android.app.`in`.haystack.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutGroupsListItemViewBinding

class EventListAdapter(val context: Context): RecyclerView.Adapter<EventListAdapter.ViewHolder>() {



    inner class ViewHolder(val binding: LayoutGroupsListItemViewBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListAdapter.ViewHolder =
        ViewHolder(LayoutGroupsListItemViewBinding.inflate(LayoutInflater.from(
            parent.context
        ), parent, false))

    override fun onBindViewHolder(holder: EventListAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 20
}