package com.android.app.`in`.haystack.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutNearestEventsItemBinding
import com.android.app.`in`.haystack.network.response.nearest_events.Data
import java.util.ArrayList

class NearestEventsAdapter(var context: Context)
    : RecyclerView.Adapter<NearestEventsAdapter.ViewHolder>() {

    private var listNearestEvents = arrayListOf<Data>()

    inner class ViewHolder(val binding: LayoutNearestEventsItemBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(nearEvents: Data) {
            binding.eventName.text = nearEvents.event_name
            binding.eventsCategory.text = nearEvents.category


        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutNearestEventsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listNearestEvents[position])
    }

    override fun getItemCount(): Int = listNearestEvents.size

    fun update(listNearestEvents: ArrayList<Data>){
        this.listNearestEvents = listNearestEvents
        notifyDataSetChanged()
    }
}