package com.android.app.`in`.haystack.view.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutGroupsListItemViewBinding

class EventsRecyclerViewAdapter(var context: Context)
    : RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder>() {

    //private var listBookings = arrayListOf<Bookings>()

    inner class ViewHolder(val binding: LayoutGroupsListItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        //@SuppressLint("SetTextI18n")
        //fun bindView(bookings: Bookings) {}
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventsRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            LayoutGroupsListItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EventsRecyclerViewAdapter.ViewHolder, position: Int) {
        //holder.bindView(listBookings!![position])
    }

    override fun getItemCount(): Int = 20


    /*fun update(context: Context, listBookings: ArrayList<Bookings>){
        this.listBookings = listBookings
        this.context = context
        notifyDataSetChanged()
    }*/
}