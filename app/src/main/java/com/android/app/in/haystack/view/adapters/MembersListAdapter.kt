package com.android.app.`in`.haystack.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.LayoutMembersListItemViewBinding
import com.android.app.`in`.haystack.network.response.group_members.Data
import com.android.app.`in`.haystack.view.fragments.MembersFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.ArrayList


class MembersListAdapter(val context: Context, val fragment: MembersFragment)
    : RecyclerView.Adapter<MembersListAdapter.ViewHolder>() {

    private var listMembers = arrayListOf<Data>()
    private lateinit var memberItemClick: MembersListItemClick


    inner class ViewHolder(val binding: LayoutMembersListItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(data: Data) {
            binding.memberName.text = data.member
            binding.memberEmail.text = data.email
            binding.memberPhone.text = data.number

            binding.deleteMember.setOnClickListener {
                showConfirmAlertDialog(data.groupid, data.id)
            }

            binding.editMember.setOnClickListener {
                memberItemClick.editMember(data.groupid, data.member, data.email, data.number)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersListAdapter.ViewHolder =
        ViewHolder(LayoutMembersListItemViewBinding.inflate(LayoutInflater.from(
            parent.context
        ), parent, false))

    override fun onBindViewHolder(holder: MembersListAdapter.ViewHolder, position: Int) {
        memberItemClick = fragment
        holder.bindView(listMembers[position])
    }

    override fun getItemCount(): Int = listMembers.size

    fun update(listMembers: ArrayList<Data>) {
        this.listMembers = listMembers
        notifyDataSetChanged()
    }

    private fun showConfirmAlertDialog(groupId: String, memberId: String) {
        val dialog = MaterialAlertDialogBuilder(context, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle("Delete Member?")
            .setMessage("Are you sure want to delete this member.?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialogInterface, i ->
                dialogInterface.dismiss()
                memberItemClick.deleteMember(groupId, memberId)
            }
            .setNegativeButton("No") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create()
        if (dialog.window != null)
            dialog.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation

        dialog.show()
    }

    interface MembersListItemClick{
        fun editMember(groupId: String, name: String, email: String, number: String)
        fun deleteMember(groupId: String, memberId: String)
    }
}