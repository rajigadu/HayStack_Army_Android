package com.android.app.`in`.haystack.view.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentMyEventsBinding
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.my_events.Data
import com.android.app.`in`.haystack.network.response.my_events.MyEvents
import com.android.app.`in`.haystack.utils.Extensions.getCurrentDate
import com.android.app.`in`.haystack.utils.Extensions.longSnackBar
import com.android.app.`in`.haystack.utils.Extensions.showErrorResponse
import com.android.app.`in`.haystack.view.viewpager.adapter.MyEventsRecyclerViewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MyEventsFragment: Fragment() {

    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var myEventsAdapter: MyEventsRecyclerViewAdapter
    private var listMyEvents = arrayListOf<Data>()
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

                            if (response.body()?.data?.size!! > 0){
                                listMyEvents.clear()
                                listMyEvents.addAll(response.body()?.data!!)
                                myEventsAdapter.update(listMyEvents)
                            }

                        }else{
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
}