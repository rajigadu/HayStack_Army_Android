package com.haystack.app.`in`.army.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haystack.app.`in`.army.databinding.LayoutNearestEventsItemBinding
import com.haystack.app.`in`.army.network.response.nearest_events.NearestEventData
import com.haystack.app.`in`.army.view.fragments.HomeFragment
import java.util.ArrayList

class NearestEventsAdapter(var context: Context)
    : RecyclerView.Adapter<NearestEventsAdapter.ViewHolder>() {

    private var listNearestEvents = arrayListOf<NearestEventData>()
    private var clickListener: NearestEventsOnClick? = null



    inner class ViewHolder(val binding: LayoutNearestEventsItemBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(nearEvents: NearestEventData) {
            binding.eventName.text = nearEvents.event_name
            binding.eventsCategory.text = nearEvents.category

            binding.nearestEventItem.setOnClickListener {
                clickListener?.nearestEventClick(nearEvents)
            }

            Glide.with(context)
                .load(nearEvents.photo)
                .into(binding.eventImage)
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

    fun update(listNearestEvents: ArrayList<NearestEventData>, listener: HomeFragment){
        this.listNearestEvents = listNearestEvents
        this.clickListener = listener
        notifyDataSetChanged()
    }

    interface NearestEventsOnClick{
        fun nearestEventClick(nearEvents: NearestEventData)
    }
}