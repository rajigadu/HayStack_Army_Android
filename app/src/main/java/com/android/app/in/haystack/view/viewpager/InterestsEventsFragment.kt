package com.android.app.`in`.haystack.view.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentInterestEventsBinding
import com.android.app.`in`.haystack.databinding.FragmentMyEventsBinding
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.interest_events.DataX
import com.android.app.`in`.haystack.network.response.interest_events.InterestEvents
import com.android.app.`in`.haystack.utils.Extensions.getCurrentDate
import com.android.app.`in`.haystack.utils.Extensions.longSnackBar
import com.android.app.`in`.haystack.utils.Extensions.showErrorResponse
import com.android.app.`in`.haystack.view.viewpager.adapter.InterestEventsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InterestsEventsFragment: Fragment() {

    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var interestEventsAdapter: InterestEventsAdapter
    private var currentDate: String? = null
    private var endTime: String? = ""
    private var listInterestEvents = arrayListOf<DataX>()


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

        interestEventsAdapter = InterestEventsAdapter(requireContext())
        binding.recyclerMyEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = interestEventsAdapter
        }

        binding.refreshMyEvents.setColorSchemeColors(ContextCompat.getColor(requireContext(),
            R.color.colorPrimary))

        binding.refreshMyEvents.setOnRefreshListener {
            listInterestEvents.clear()
            interestEvents()
        }

    }

    private fun interestEvents() {
        binding.refreshMyEvents.isRefreshing = true
        currentDate = getCurrentDate()
        Repository.getInterestEvents(currentDate!!, endTime!!).enqueue(
            object : Callback<InterestEvents>{
                override fun onResponse(
                    call: Call<InterestEvents>,
                    response: Response<InterestEvents>
                ) {

                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){
                                if (response.body()?.data?.size!! > 0) {
                                    listInterestEvents.clear()
                                    listInterestEvents.addAll(response.body()?.data!!)
                                    interestEventsAdapter.update(listInterestEvents)
                                }else{

                                }
                            }else{
                                longSnackBar(response.body()?.message!!, binding.constraintMyEvents)
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                    binding.refreshMyEvents.isRefreshing = false
                }

                override fun onFailure(call: Call<InterestEvents>, t: Throwable) {
                    showErrorResponse(t, binding.constraintMyEvents)
                    binding.refreshMyEvents.isRefreshing = false
                }

            })
    }


    override fun onResume() {
        super.onResume()
        interestEvents()
    }

}