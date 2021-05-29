package com.android.app.`in`.haystack.view.viewpager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutGroupsListItemViewBinding
import com.android.app.`in`.haystack.network.response.my_events.Data
import java.util.ArrayList

class MyEventsRecyclerViewAdapter(var context: Context)
    : RecyclerView.Adapter<MyEventsRecyclerViewAdapter.ViewHolder>() {

    private var listMyEvents = arrayListOf<Data>()

    inner class ViewHolder(val binding: LayoutGroupsListItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(myEvents: Data) {
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
        holder.bindView(listMyEvents[position])
    }

    override fun getItemCount(): Int = listMyEvents.size

    fun update(listMyEvents: ArrayList<Data>){
        this.listMyEvents = listMyEvents
        notifyDataSetChanged()
    }
}