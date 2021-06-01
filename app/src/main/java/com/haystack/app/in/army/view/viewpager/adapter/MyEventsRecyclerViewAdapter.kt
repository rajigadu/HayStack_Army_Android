package com.haystack.app.`in`.army.view.viewpager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haystack.app.`in`.army.databinding.LayoutGroupsListItemViewBinding
import com.haystack.app.`in`.army.network.response.my_events.MyEventsData
import com.haystack.app.`in`.army.view.viewpager.MyEventsFragment
import java.util.ArrayList

class MyEventsRecyclerViewAdapter(var context: Context)
    : RecyclerView.Adapter<MyEventsRecyclerViewAdapter.ViewHolder>() {

    private var listMyEvents = arrayListOf<MyEventsData>()
    private var eventsOnClick: MyEventsOnClickListener? = null



    inner class ViewHolder(val binding: LayoutGroupsListItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(myEvents: MyEventsData) {
            binding.eventName.text = myEvents.event_name
            binding.membersCount.text = "People (${myEvents.membercount})"

            binding.eventItem.setOnClickListener {
                eventsOnClick?.myEventsItemCLick(myEvents)
            }

            binding.editEventGroup.setOnClickListener {
                eventsOnClick?.editMyEvent(myEvents)
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutGroupsListItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listMyEvents[position])
    }

    override fun getItemCount(): Int = listMyEvents.size

    fun update(listMyEvents: ArrayList<MyEventsData>, clickListener: MyEventsFragment){
        this.listMyEvents = listMyEvents
        this.eventsOnClick = clickListener
        notifyDataSetChanged()
    }

    interface MyEventsOnClickListener{
        fun myEventsItemCLick(events: MyEventsData)
        fun deleteMyEvent()
        fun editMyEvent(events: MyEventsData)
    }
}