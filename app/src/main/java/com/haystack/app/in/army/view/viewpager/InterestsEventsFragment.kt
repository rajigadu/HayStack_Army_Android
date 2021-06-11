package com.haystack.app.`in`.army.view.viewpager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.haystack.app.`in`.army.R
import com.haystack.app.`in`.army.databinding.FragmentMyEventsBinding
import com.haystack.app.`in`.army.manager.SessionManager
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.group_members.DefaultResponse
import com.haystack.app.`in`.army.network.response.interest_events.InterestEventsData
import com.haystack.app.`in`.army.network.response.interest_events.InterestEvents
import com.haystack.app.`in`.army.utils.AppConstants
import com.haystack.app.`in`.army.utils.Extensions
import com.haystack.app.`in`.army.utils.Extensions.getCurrentDate
import com.haystack.app.`in`.army.utils.Extensions.longSnackBar
import com.haystack.app.`in`.army.utils.Extensions.showAlertDialog
import com.haystack.app.`in`.army.utils.Extensions.showErrorResponse
import com.haystack.app.`in`.army.view.viewpager.adapter.InterestEventsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InterestsEventsFragment: Fragment(), InterestEventsAdapter.InterestedEventsItemClick {

    private lateinit var binding: FragmentMyEventsBinding
    private lateinit var interestEventsAdapter: InterestEventsAdapter
    private lateinit var bottomSheet: BottomSheetDialog
    private var currentDate: String? = null
    private var endTime: String? = ""
    private var listInterestEvents = arrayListOf<InterestEventsData>()


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
                                binding.noEventsImage.visibility = View.INVISIBLE
                                binding.noEventsText.visibility = View.INVISIBLE

                                if (response.body()?.data != null) {
                                    listInterestEvents.clear()
                                    listInterestEvents.addAll(listOf(response.body()?.data!!))
                                    interestEventsAdapter.update(listInterestEvents, this@InterestsEventsFragment)
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
                    showErrorResponse(t, binding.constraintMyEvents)
                    binding.refreshMyEvents.isRefreshing = false
                }

            })
    }


    override fun onResume() {
        super.onResume()
        interestEvents()
    }

    override fun deleteInterestedEvent(interestEvent: InterestEventsData) {
        showBottomSheet()
        Repository.deleteOtherEvents(interestEvent.id, SessionManager.instance.getUserId(),
            AppConstants.EVENT_TYPE_INTEREST)
            .enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    try {

                        if (response.isSuccessful){
                            if (response.body()?.status == "1"){
                                showAlertDialog(
                                    "Delete Event",
                                    requireContext(),
                                    response.body()?.message
                                )
                                interestEvents()
                            }else{
                                showAlertDialog(
                                    "Delete Event",
                                    requireContext(),
                                    response.body()?.message
                                )
                            }
                        }

                    }catch (e: Exception){e.printStackTrace()}
                    hideBottomSheet()
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Extensions.showSnackBar(binding.constraintMyEvents, "something went wrong")
                    hideBottomSheet()
                }

            })
    }

    @SuppressLint("SetTextI18n")
    private fun showBottomSheet(){
        bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(requireContext().applicationContext)
            .inflate(
                R.layout.authentication_progress_bottom_sheet,
                requireActivity().findViewById<ConstraintLayout>(R.id.bottom_sheet)
            )
        val title = view.findViewById<TextView>(R.id.progress_title)
        val subtitle = view.findViewById<TextView>(R.id.progress_sub_title)

        title.text = "Deleting Event"
        subtitle.text = "Deleting event, please wait...."

        bottomSheet.setCancelable(false)
        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private  fun hideBottomSheet(){
        bottomSheet.hide()
    }

}