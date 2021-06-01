package com.haystack.app.`in`.army.view.viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.haystack.app.`in`.army.R
import com.haystack.app.`in`.army.databinding.FragmentMyEventsBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.attend_events.AttendEvents
import com.haystack.app.`in`.army.network.response.attend_events.Data
import com.haystack.app.`in`.army.utils.Extensions
import com.haystack.app.`in`.army.utils.Extensions.getCurrentDate
import com.haystack.app.`in`.army.utils.Extensions.longSnackBar
import com.haystack.app.`in`.army.view.viewpager.adapter.AttendEventsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendEventsFragment: Fragment() {

    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var attendEventsAdapter: AttendEventsAdapter
    private var currentDate: String? = null
    private var endTime: String? = ""
    private var listAttendEvents = arrayListOf<Data>()


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

        binding.refreshMyEvents.setColorSchemeColors(
            ContextCompat.getColor(requireContext(),
            R.color.colorPrimary))

        binding.refreshMyEvents.setOnRefreshListener {
            listAttendEvents.clear()
            attendedEvents()
        }

        attendEventsAdapter = AttendEventsAdapter(requireContext())
        binding.recyclerMyEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = attendEventsAdapter
        }
    }

    private fun attendedEvents() {
        currentDate = getCurrentDate()
        binding.refreshMyEvents.isRefreshing = true
        Repository.getAttendEvents(currentDate!!, endTime!!).enqueue(
            object : Callback<AttendEvents> {
                override fun onResponse(
                    call: Call<AttendEvents>,
                    response: Response<AttendEvents>
                ) {
                    Log.e("TAG", "response: $response")
                    try {
                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){

                                binding.noEventsImage.visibility = INVISIBLE
                                binding.noEventsText.visibility = INVISIBLE

                                if (response.body()?.data != null){
                                    listAttendEvents.clear()
                                    listAttendEvents.addAll(listOf(response.body()?.data!!))
                                }

                            }else{
                                binding.noEventsImage.visibility = VISIBLE
                                binding.noEventsText.visibility = VISIBLE
                                longSnackBar(response.body()?.message!!, binding.constraintMyEvents)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                    binding.refreshMyEvents.isRefreshing = false
                }

                override fun onFailure(call: Call<AttendEvents>, t: Throwable) {
                    Extensions.showErrorResponse(t, binding.constraintMyEvents)
                    binding.refreshMyEvents.isRefreshing = false
                }

            })
    }

    override fun onResume() {
        super.onResume()
        attendedEvents()
    }
}