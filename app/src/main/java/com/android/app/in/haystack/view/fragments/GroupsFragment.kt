package com.android.app.`in`.haystack.view.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentGroupsBinding
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.android.app.`in`.haystack.view.adapters.EventListAdapter

class GroupsFragment: Fragment(), EventListAdapter.EventGroupItemClickListener {


    private lateinit var binding: FragmentGroupsBinding
    private lateinit var eventListAdapter: EventListAdapter
    private var splash_delay: Long  = 3000


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

        binding.recyclerEvents.visibility = INVISIBLE
        binding.btnCreateGroup.visibility = VISIBLE
        binding.imageView6.visibility = VISIBLE
        Handler(Looper.getMainLooper()).postDelayed(mRunnable, splash_delay)

        binding.btnCreateGroup.setOnClickListener {
            findNavController().navigate(R.id.action_groupsFragment_to_createGroup)
        }

    }

    private var mRunnable = Runnable {
        if (!requireActivity().isFinishing){
            showGroups()
        }
    }

    private fun showGroups(){
        binding.recyclerEvents.visibility = VISIBLE
        binding.btnCreateGroup.visibility = INVISIBLE
        binding.imageView6.visibility = INVISIBLE

        eventListAdapter = EventListAdapter(requireContext(), this)
        binding.recyclerEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventListAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).updateBottomNavChange(1)
        (activity as MainMenuActivity).showBottomNav()
    }

    override fun groupItemEdit() {
        findNavController().navigate(R.id.action_groupsFragment_to_editGroup)
    }

    override fun membersViewClick() {
        findNavController().navigate(R.id.action_groupsFragment_to_membersFragment)
    }
}