package com.haystack.app.`in`.army.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.haystack.app.`in`.army.R
import com.haystack.app.`in`.army.databinding.FragmentGroupsBinding
import com.haystack.app.`in`.army.manager.SessionManager
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.all_groups.AllGroups
import com.haystack.app.`in`.army.network.response.all_groups.Data
import com.haystack.app.`in`.army.network.response.group_members.DefaultResponse
import com.haystack.app.`in`.army.utils.AppConstants.GROUP_ID
import com.haystack.app.`in`.army.utils.Extensions.showAlertDialog
import com.haystack.app.`in`.army.utils.Extensions.showSnackBar
import com.haystack.app.`in`.army.utils.RecyclerViewCustomAnimation
import com.haystack.app.`in`.army.view.activity.MainMenuActivity
import com.haystack.app.`in`.army.view.adapters.EventListAdapter
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
    ): View {
        binding = FragmentGroupsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.refreshGroupList.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        binding.refreshGroupList.setOnRefreshListener {
            listGroups.clear()
            getAllGroups()
        }

        binding.toolbarGroups.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.addMember -> {
                    findNavController().navigate(R.id.action_groupsFragment_to_editMember)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }

        eventListAdapter = EventListAdapter(requireContext(), this)
        binding.recyclerEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventListAdapter
            itemAnimator = RecyclerViewCustomAnimation()
        }

        binding.btnCreateGroup.setOnClickListener {
            findNavController().navigate(R.id.action_groupsFragment_to_createGroup)
        }

    }

    private fun getAllGroups() {
        binding.refreshGroupList.isRefreshing = true
        Repository.getAllGroupsList(SessionManager.instance.getUserId())
            .enqueue(object : Callback<AllGroups>{
                override fun onResponse(call: Call<AllGroups>, response: Response<AllGroups>) {
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
                                showAlertDialog(
                                    "Some Error Occurred!",
                                    requireContext(),
                                    response.body()?.message
                                )
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}

                    binding.refreshGroupList.isRefreshing = false
                }

                override fun onFailure(call: Call<AllGroups>, t: Throwable) {
                    showSnackBar(binding.constraintGroups, t.localizedMessage!!)
                    binding.refreshGroupList.isRefreshing = false
                }

            })
    }

    private fun showEmptyGroup(){
        binding.refreshGroupList.visibility = INVISIBLE
        binding.btnCreateGroup.visibility = VISIBLE
        binding.imageView6.visibility = VISIBLE
    }

    private fun showGroupList(){
        binding.refreshGroupList.visibility = VISIBLE
        binding.btnCreateGroup.visibility = INVISIBLE
        binding.imageView6.visibility = INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        getAllGroups()
        (activity as MainMenuActivity).updateBottomNavChange(1)
        (activity as MainMenuActivity).showBottomNav()
    }

    override fun groupItemEdit(groupId: String) {
        val bundle = bundleOf(GROUP_ID to groupId)
        findNavController().navigate(R.id.action_groupsFragment_to_editGroup, bundle)
    }

    override fun membersViewClick(groupId: String) {
        val bundle = bundleOf(GROUP_ID to groupId)
        findNavController().navigate(R.id.action_groupsFragment_to_membersFragment, bundle)
    }

    override fun deleteGroup(groupId: String) {
        binding.refreshGroupList.isRefreshing = true
        Repository.deleteGroup(groupId, com.haystack.app.`in`.army.manager.SessionManager.instance.getUserId())
            .enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){

                                showSnackBar(binding.constraintGroups, response.body()?.message!!)

                                getAllGroups()

                            }else{
                                showAlertDialog("Failed!", requireContext(), response.body()?.message)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                    binding.refreshGroupList.isRefreshing = false
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    showSnackBar(binding.constraintGroups, t.localizedMessage!!)
                    binding.refreshGroupList.isRefreshing = false
                }

            })
    }
}