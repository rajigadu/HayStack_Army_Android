package com.android.app.`in`.haystack.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutGroupsListItemViewBinding
import com.android.app.`in`.haystack.view.fragments.GroupsFragment

class EventListAdapter(val context: Context, val fragment: GroupsFragment)
    : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    private lateinit var groupItemClick: EventGroupItemClickListener


    inner class ViewHolder(val binding: LayoutGroupsListItemViewBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListAdapter.ViewHolder =
        ViewHolder(LayoutGroupsListItemViewBinding.inflate(LayoutInflater.from(
            parent.context
        ), parent, false))

    override fun onBindViewHolder(holder: EventListAdapter.ViewHolder, position: Int) {
        groupItemClick = fragment
        holder.binding.editEventGroup.setOnClickListener {
            groupItemClick.groupItemEdit()
        }

        holder.binding.members.setOnClickListener {
            groupItemClick.membersViewClick()
        }
    }

    override fun getItemCount(): Int = 20

    interface EventGroupItemClickListener{
        fun groupItemEdit()
        fun membersViewClick()
    }
}