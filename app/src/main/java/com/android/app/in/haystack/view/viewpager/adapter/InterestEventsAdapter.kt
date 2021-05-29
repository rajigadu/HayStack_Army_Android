package com.android.app.`in`.haystack.view.viewpager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutGroupsListItemViewBinding
import com.android.app.`in`.haystack.network.response.interest_events.DataX
import java.util.ArrayList

class InterestEventsAdapter(var context: Context)
    : RecyclerView.Adapter<InterestEventsAdapter.ViewHolder>() {

    private var listInterestEvents = arrayListOf<DataX>()

    inner class ViewHolder(val binding: LayoutGroupsListItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(myEvents: DataX) {
            binding.eventName.text = myEvents.event_name
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
        holder.bindView(listInterestEvents[position])
    }

    override fun getItemCount(): Int = listInterestEvents.size

    fun update(listMyEvents: ArrayList<DataX>){
        this.listInterestEvents = listMyEvents
        notifyDataSetChanged()
    }
}