package com.android.app.`in`.haystack.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutEventSearchListItemBinding
import com.android.app.`in`.haystack.databinding.LayoutMembersListItemViewBinding
import com.android.app.`in`.haystack.view.fragments.EventsSearch


class EventSearchListAdapter(val context: Context, val fragment: EventsSearch)
    : RecyclerView.Adapter<EventSearchListAdapter.ViewHolder>() {

    private lateinit var eventListItemClick: EventSearchListItemClick


    inner class ViewHolder(val binding: LayoutEventSearchListItemBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSearchListAdapter.ViewHolder =
        ViewHolder(LayoutEventSearchListItemBinding.inflate(LayoutInflater.from(
            parent.context
        ), parent, false))

    override fun onBindViewHolder(holder: EventSearchListAdapter.ViewHolder, position: Int) {
        eventListItemClick = fragment
        holder.binding.eventItem.setOnClickListener {
            eventListItemClick.eventListItemClick()
        }
    }

    override fun getItemCount(): Int = 20

    interface EventSearchListItemClick{
        fun eventListItemClick()
    }

}