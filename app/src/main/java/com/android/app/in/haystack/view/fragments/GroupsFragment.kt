package com.android.app.`in`.haystack.view.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentGroupsBinding
import com.android.app.`in`.haystack.manager.SessionManager
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.all_groups.AllGroups
import com.android.app.`in`.haystack.network.response.all_groups.Data
import com.android.app.`in`.haystack.utils.AppConstants.GROUP_ID
import com.android.app.`in`.haystack.utils.Extensions
import com.android.app.`in`.haystack.utils.Extensions.showSnackBar
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.android.app.`in`.haystack.view.adapters.EventListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupsFragment: Fragment(), EventListAdapter.EventGroupItemClickListener {


    private lateinit var binding: FragmentGroupsBinding
    private lateinit var eventListAdapter: EventListAdapter
    private var listGroups = arrayListOf<Data>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllGroups()

        eventListAdapter = EventListAdapter(requireContext(), this)
        binding.recyclerEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventListAdapter
        }

        binding.btnCreateGroup.setOnClickListener {
            findNavController().navigate(R.id.action_groupsFragment_to_createGroup)
        }

    }

    private fun getAllGroups() {
        Log.e("TAG", "userId: "+SessionManager.instance.getUserId())
        Repository.getAllGroupsList(SessionManager.instance.getUserId())
            .enqueue(object : Callback<AllGroups>{
                override fun onResponse(call: Call<AllGroups>, response: Response<AllGroups>) {
                    Log.e("TAG", "response: "+response.body()?.data)
                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){

                                if (response.body()?.data?.size!! > 0){
                                    showGroupList()
                                    listGroups.clear()
                                    listGroups.addAll(response.body()?.data!!)
                                    eventListAdapter.updateGroupList(listGroups)
                                }else{
                                    showEmptyGroup()
                                }

                            }else{
                                Extensions.showAlertDialog(
                                    "Some Error Occurred!",
                                    requireContext(),
                                    response.body()?.message
                                )
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                }

                override fun onFailure(call: Call<AllGroups>, t: Throwable) {
                    showSnackBar(binding.constraintGroups, t.localizedMessage!!)
                }

            })
    }

    private fun showEmptyGroup(){
        binding.recyclerEvents.visibility = INVISIBLE
        binding.btnCreateGroup.visibility = VISIBLE
        binding.imageView6.visibility = VISIBLE
    }

    private fun showGroupList(){
        binding.recyclerEvents.visibility = VISIBLE
        binding.btnCreateGroup.visibility = INVISIBLE
        binding.imageView6.visibility = INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).updateBottomNavChange(1)
        (activity as MainMenuActivity).showBottomNav()
    }

    override fun groupItemEdit(groupId: String) {
        val bundle = bundleOf(GROUP_ID to groupId)
        findNavController().navigate(R.id.action_groupsFragment_to_editGroup, bundle)
    }

    override fun membersViewClick() {
        findNavController().navigate(R.id.action_groupsFragment_to_membersFragment)
    }
}