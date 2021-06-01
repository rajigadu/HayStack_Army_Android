package com.haystack.app.`in`.army.view.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.haystack.app.`in`.army.R
import com.haystack.app.`in`.army.databinding.FragmentMyEventsBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.interest_events.DataX
import com.haystack.app.`in`.army.network.response.interest_events.InterestEvents
import com.haystack.app.`in`.army.utils.Extensions
import com.haystack.app.`in`.army.utils.Extensions.getCurrentDate
import com.haystack.app.`in`.army.utils.Extensions.longSnackBar
import com.haystack.app.`in`.army.view.viewpager.adapter.InvitedEventsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvitedEventsFragment: Fragment() {

    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var invitedEventsAdapter: InvitedEventsAdapter
    private var currentDate: String? = null
    private var endTime: String? = ""
    private var listInvitedEvents = arrayListOf<DataX>()


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

        binding.refreshMyEvents.setColorSchemeColors(ContextCompat.getColor(requireContext(),
            R.color.colorPrimary))

        binding.refreshMyEvents.setOnRefreshListener {
            listInvitedEvents.clear()
            invitedEvents()
        }

        invitedEventsAdapter = InvitedEventsAdapter(requireContext())
        binding.recyclerMyEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = invitedEventsAdapter
        }


    }

    private fun invitedEvents() {
        binding.refreshMyEvents.isRefreshing = true
        currentDate = getCurrentDate()
        Repository.getInterestEvents(currentDate!!, endTime!!).enqueue(
            object : Callback<InterestEvents> {
                override fun onResponse(
                    call: Call<InterestEvents>,
                    response: Response<InterestEvents>
                ) {

                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){
                                binding.noEventsImage.visibility = View.INVISIBLE
                                binding.noEventsText.visibility = View.INVISIBLE

                                if (response.body()?.data != null){
                                    listInvitedEvents.clear()
                                    listInvitedEvents.addAll(listOf(response.body()?.data!!))
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

                override fun onFailure(call: Call<InterestEvents>, t: Throwable) {
                    Extensions.showErrorResponse(t, binding.constraintMyEvents)
                    binding.refreshMyEvents.isRefreshing = false
                }

            })
    }

    override fun onResume() {
        super.onResume()
        invitedEvents()
    }
}