package com.android.app.`in`.haystack.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentEventSearchBinding
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.search_events.Data
import com.android.app.`in`.haystack.network.response.search_events.SearchByEvent
import com.android.app.`in`.haystack.network.response.search_events.SearchEvents
import com.android.app.`in`.haystack.utils.AppConstants.ARG_SERIALIZABLE
import com.android.app.`in`.haystack.utils.Extensions.longSnackBar
import com.android.app.`in`.haystack.utils.Extensions.showErrorResponse
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.android.app.`in`.haystack.view.adapters.EventSearchListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsSearch: Fragment(), EventSearchListAdapter.EventSearchListItemClick {

    private lateinit var binding: FragmentEventSearchBinding
    private lateinit var eventSearchListAdapter: EventSearchListAdapter
    private lateinit var searchEvent: SearchByEvent

    private var listSearchedEvents = arrayListOf<Data>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventSearchBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.refreshSearchEvents.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        searchEvent = arguments?.getSerializable(ARG_SERIALIZABLE) as SearchByEvent

        binding.toolbarEventsSearch.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        eventSearchListAdapter = EventSearchListAdapter(requireContext(), this)
        binding.eventsSearchListView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventSearchListAdapter
        }

        binding.refreshSearchEvents.setOnRefreshListener {
            listSearchedEvents.clear()
            getSearchEvents()
        }

    }

    private fun getSearchEvents() {
        binding.refreshSearchEvents.isRefreshing = true
        Repository.searchEvent(searchEvent).enqueue(object : Callback<SearchEvents>{
            override fun onResponse(call: Call<SearchEvents>, response: Response<SearchEvents>) {

                try {

                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){

                            if (response.body()?.data != null){
                                listSearchedEvents.clear()
                                listSearchedEvents.addAll(response.body()?.data!!)
                                eventSearchListAdapter.update(listSearchedEvents)
                            }

                        }else{
                            longSnackBar(response.body()?.message!!, binding.constraintEventSearch)
                        }
                    }

                }catch (e: Exception){e.printStackTrace()}
                binding.refreshSearchEvents.isRefreshing = false
            }

            override fun onFailure(call: Call<SearchEvents>, t: Throwable) {
                showErrorResponse(t, binding.constraintEventSearch)
                binding.refreshSearchEvents.isRefreshing = false
            }

        })
    }

    override fun eventListItemClick() {
        findNavController().navigate(R.id.action_eventsSearch_to_eventsInfoFragment)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
        getSearchEvents()
    }
}