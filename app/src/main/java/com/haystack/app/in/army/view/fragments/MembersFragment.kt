package com.haystack.app.`in`.army.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.haystack.app.`in`.army.R
import com.haystack.app.`in`.army.databinding.FragmentMembersBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.group_members.Data
import com.haystack.app.`in`.army.network.response.group_members.DefaultResponse
import com.haystack.app.`in`.army.network.response.group_members.GroupMembers
import com.haystack.app.`in`.army.utils.AppConstants.GROUP_ID
import com.haystack.app.`in`.army.utils.AppConstants.MEMBER_EMAIL
import com.haystack.app.`in`.army.utils.AppConstants.MEMBER_NAME
import com.haystack.app.`in`.army.utils.AppConstants.MEMBER_PHONE
import com.haystack.app.`in`.army.utils.AppConstants.STATUS
import com.haystack.app.`in`.army.utils.Extensions.showAlertDialog
import com.haystack.app.`in`.army.utils.Extensions.showSnackBar
import com.haystack.app.`in`.army.utils.RecyclerViewCustomAnimation
import com.haystack.app.`in`.army.view.activity.MainMenuActivity
import com.haystack.app.`in`.army.view.adapters.MembersListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MembersFragment: Fragment(), MembersListAdapter.MembersListItemClick {


    private lateinit var binding: FragmentMembersBinding
    private lateinit var membersAdapter: MembersListAdapter
    private var listMembers = arrayListOf<Data>()
    private var groupId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMembersBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupId = arguments?.getString(GROUP_ID)

        binding.refreshMembersList.setColorSchemeColors(ContextCompat.getColor(requireContext(),
            R.color.colorPrimary))

        binding.refreshMembersList.setOnRefreshListener {
            listMembers.clear()
            getGroupMembersList()
        }

        membersAdapter = MembersListAdapter(requireContext(), this)
        binding.recyclerViewMembers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = membersAdapter
            itemAnimator = RecyclerViewCustomAnimation()
        }

        binding.toolbarMembers.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbarMembers.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.addMember -> {
                    val bundle = bundleOf(STATUS to "0")
                    findNavController().navigate(R.id.action_membersFragment_to_editMember, bundle)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
    }

    private fun getGroupMembersList() {
        binding.refreshMembersList.isRefreshing = true
        Repository.getGroupMembers(groupId!!, com.haystack.app.`in`.army.manager.SessionManager.instance.getUserId())
            .enqueue(object : Callback<GroupMembers>{
                override fun onResponse(
                    call: Call<GroupMembers>,
                    response: Response<GroupMembers>
                ) {
                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){
                                if (response.body()?.data?.size!! > 0){
                                    listMembers.clear()
                                    listMembers.addAll(response.body()?.data!!)
                                    membersAdapter.update(listMembers)
                                }

                            }else{
                                showAlertDialog("Failed!", requireContext(), response.body()?.message)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}

                    binding.refreshMembersList.isRefreshing = false
                }

                override fun onFailure(call: Call<GroupMembers>, t: Throwable) {
                    showSnackBar(binding.constraintMembers, t.localizedMessage!!)
                    binding.refreshMembersList.isRefreshing = false
                }

            })
    }

    override fun onResume() {
        super.onResume()
        getGroupMembersList()
        (activity as MainMenuActivity).hideBottomNav()
    }

    override fun editMember(groupId: String, name: String, email: String, phone: String) {
        //val bundle = bundleOf(GROUP_ID to groupId)
        val bundle = bundleOf(
            STATUS to "1",
            GROUP_ID to groupId,
            MEMBER_NAME to name,
            MEMBER_EMAIL to email,
            MEMBER_PHONE to phone
        )
        findNavController().navigate(R.id.action_membersFragment_to_editMember, bundle)
    }

    override fun deleteMember(groupId: String, memberId: String) {
        Repository.deleteGroupMember(groupId, memberId, com.haystack.app.`in`.army.manager.SessionManager.instance.getUserId())
            .enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {

                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){

                                showSnackBar(binding.constraintMembers, response.body()?.message!!)

                                getGroupMembersList()

                            }else{
                                showAlertDialog("Failed!", requireContext(), response.body()?.message)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    showSnackBar(binding.constraintMembers, t.localizedMessage!!)
                }

            })
    }
}