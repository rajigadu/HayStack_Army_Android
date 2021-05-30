package com.android.app.`in`.haystack.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutEventSearchListItemBinding
import com.android.app.`in`.haystack.databinding.LayoutMembersListItemViewBinding
import com.android.app.`in`.haystack.network.response.search_events.Data
import com.android.app.`in`.haystack.view.fragments.EventsSearch
import java.util.ArrayList


class EventSearchListAdapter(val context: Context, val fragment: EventsSearch)
    : RecyclerView.Adapter<EventSearchListAdapter.ViewHolder>() {

    private lateinit var eventListItemClick: EventSearchListItemClick
    private var listEvents = arrayListOf<Data>()


    inner class ViewHolder(val binding: LayoutEventSearchListItemBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(data: Data) {

            binding.eventManag.text = data.event_name
            binding.hostName.text = data.hostname
            binding.hostNumber.text = data.contactinfo
            binding.membersCount.text = "People (${data.membercount})"

            binding.eventItem.setOnClickListener {
                eventListItemClick.eventListItemClick()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventSearchListAdapter.ViewHolder =
        ViewHolder(LayoutEventSearchListItemBinding.inflate(LayoutInflater.from(
            parent.context
        ), parent, false))

    override fun onBindViewHolder(holder: EventSearchListAdapter.ViewHolder, position: Int) {
        eventListItemClick = fragment
        holder.bindView(listEvents[position])
    }

    override fun getItemCount(): Int = listEvents.size

    fun update(listSearchedEvents: ArrayList<Data>) {
        this.listEvents = listSearchedEvents
        notifyDataSetChanged()
    }

    interface EventSearchListItemClick{
        fun eventListItemClick()
    }

}