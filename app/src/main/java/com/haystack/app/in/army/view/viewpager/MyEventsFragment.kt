package com.haystack.app.`in`.army.view.viewpager

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
import com.haystack.app.`in`.army.databinding.FragmentMyEventsBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.my_events.MyEvents
import com.haystack.app.`in`.army.network.response.my_events.MyEventsData
import com.haystack.app.`in`.army.utils.AppConstants.ARG_OBJECTS
import com.haystack.app.`in`.army.utils.AppConstants.ARG_SERIALIZABLE
import com.haystack.app.`in`.army.utils.Extensions.getCurrentDate
import com.haystack.app.`in`.army.utils.Extensions.longSnackBar
import com.haystack.app.`in`.army.utils.Extensions.showErrorResponse
import com.haystack.app.`in`.army.utils.RecyclerViewCustomAnimation
import com.haystack.app.`in`.army.view.viewpager.adapter.MyEventsRecyclerViewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MyEventsFragment: Fragment(), MyEventsRecyclerViewAdapter.MyEventsOnClickListener {

    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var myEventsAdapter: MyEventsRecyclerViewAdapter
    private var listMyEvents = arrayListOf<MyEventsData>()
    private var endTime: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyEventsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.refreshMyEvents.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        binding.refreshMyEvents.setOnRefreshListener {
            listMyEvents.clear()
            getMyEvents()
        }

        myEventsAdapter = MyEventsRecyclerViewAdapter(requireContext())
        binding.recyclerMyEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myEventsAdapter
        }

    }

    private fun getMyEvents() {
        binding.refreshMyEvents.isRefreshing = true
        val currentDate = getCurrentDate()
        Repository.getMyEvents(currentDate, endTime = endTime!!).enqueue(object :
        Callback<MyEvents>{
            override fun onResponse(call: Call<MyEvents>, response: Response<MyEvents>) {
                try {

                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){
                            binding.noEventsImage.visibility = View.INVISIBLE
                            binding.noEventsText.visibility = View.INVISIBLE

                            if (response.body()?.data?.size!! > 0){
                                listMyEvents.clear()
                                listMyEvents.addAll(response.body()?.data!!)
                                myEventsAdapter.update(listMyEvents, this@MyEventsFragment)
                            }

                        }else{
                            binding.noEventsImage.visibility = View.VISIBLE
                            binding.noEventsText.visibility = View.VISIBLE
                            longSnackBar(response.body()?.message!!, binding.constraintMyEvents)
                        }
                    }

                }catch (e: Exception){e.printStackTrace()}
                binding.refreshMyEvents.isRefreshing = false
            }

            override fun onFailure(call: Call<MyEvents>, t: Throwable) {
                showErrorResponse(t, binding.constraintMyEvents)
                binding.refreshMyEvents.isRefreshing = false
            }

        })
    }

    override fun onResume() {
        super.onResume()
        getMyEvents()
    }

    override fun myEventsItemCLick(events: MyEventsData) {
        val bundle = bundleOf(
            ARG_OBJECTS to "My Events",
            ARG_SERIALIZABLE to events
        )
        findNavController().navigate(R.id.eventsInfoFragment, bundle)
    }

    override fun deleteMyEvent() {
    }

    override fun editMyEvent(events: MyEventsData) {
        val bundle = bundleOf(ARG_SERIALIZABLE to events)
        findNavController().navigate(R.id.action_myEvents_to_editEvents, bundle)
    }
}