package com.android.app.`in`.haystack.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.LayoutGroupsListItemViewBinding
import com.android.app.`in`.haystack.network.response.all_groups.Data
import com.android.app.`in`.haystack.view.fragments.GroupsFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EventListAdapter(val context: Context, val fragment: GroupsFragment)
    : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    private lateinit var groupItemClick: EventGroupItemClickListener
    private var listGroups = arrayListOf<Data>()


    inner class ViewHolder(val binding: LayoutGroupsListItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(data: Data) {
            binding.groupName.text = data.gname
            binding.membersCount.text = "People (${data.membercount})"

            binding.editEventGroup.setOnClickListener {
                groupItemClick.groupItemEdit(data.id)
            }

            binding.members.setOnClickListener {
                groupItemClick.membersViewClick(data.id)
            }

            binding.deleteGroup.setOnClickListener {
                showConfirmAlertDialog(data.id)
            }
        }
    }

    private fun showConfirmAlertDialog(id: String) {
        val dialog = MaterialAlertDialogBuilder(context, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle("Delete Group?")
            .setMessage("Are you sure want to delete this group.?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialogInterface, i ->
                dialogInterface.dismiss()
                groupItemClick.deleteGroup(id)
            }
            .setNegativeButton("No") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()
        if (dialog.window != null)
            dialog.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation

        dialog.show()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListAdapter.ViewHolder =
        ViewHolder(LayoutGroupsListItemViewBinding.inflate(LayoutInflater.from(
            parent.context
        ), parent, false))

    override fun onBindViewHolder(holder: EventListAdapter.ViewHolder, position: Int) {
        groupItemClick = fragment

        holder.bindView(listGroups[position])
    }

    fun updateGroupList(groupList: ArrayList<Data>){
        this.listGroups = groupList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listGroups.size

    interface EventGroupItemClickListener{
        fun groupItemEdit(groupId: String)
        fun membersViewClick(groupId: String)
        fun deleteGroup(groupId: String)
    }
}