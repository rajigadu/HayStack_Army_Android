package com.android.app.`in`.haystack.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentDateRangeBinding
import com.android.app.`in`.haystack.network.response.search_events.SearchByEvent
import com.android.app.`in`.haystack.utils.AppConstants.ARG_SERIALIZABLE
import com.android.app.`in`.haystack.utils.Extensions
import com.android.app.`in`.haystack.utils.Extensions.longSnackBar
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class DateRangeFragment: Fragment() {


    private lateinit var binding: FragmentDateRangeBinding
    private var searchEvent: SearchByEvent? = null
    private var lastClickTime: Long = 0

    private var startDate: String? = null
    private var startTime: String? = null
    private var endDate: String? = null
    private var endTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDateRangeBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchEvent = arguments?.getSerializable(ARG_SERIALIZABLE) as SearchByEvent
        Log.e("TAG", "searchEvent: $searchEvent")

        clickListeners()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {

        binding.toolbarDateRange.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnContinue.setOnClickListener {
            if (validated()){
                val bundle = bundleOf(ARG_SERIALIZABLE to searchEvent)
                findNavController().navigate(R.id.action_dateRangeFragment_to_eventsSearch, bundle)
            }

        }

        binding.inputStartTime.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_UP -> {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                        return@setOnTouchListener false
                    }
                    lastClickTime = SystemClock.elapsedRealtime()
                    showTimePickerDialog("Select Event Start Time")
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }

        binding.inputEndTime.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_UP -> {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                        return@setOnTouchListener false
                    }
                    lastClickTime = SystemClock.elapsedRealtime()
                    showTimePickerDialog("Select Event End Time")
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }

        binding.inputStartDate.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_UP -> {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                        return@setOnTouchListener false
                    }
                    lastClickTime = SystemClock.elapsedRealtime()
                    showDatePickerDialog("Select Event Start Date")
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }

        binding.inputEndDate.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_UP -> {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                        return@setOnTouchListener false
                    }
                    lastClickTime = SystemClock.elapsedRealtime()
                    showDatePickerDialog("Select Event End Date")
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }
    }

    private fun validated(): Boolean {
        when{
            searchEvent?.startDate!!.isEmpty() -> {
                longSnackBar("Please select start data", binding.constraintDateRange)
                return false
            }
            searchEvent?.startTime!!.isEmpty() -> {
                longSnackBar("Please select start time", binding.constraintDateRange)
                return false
            }
            searchEvent?.endTime!!.isEmpty() -> {
                longSnackBar("Please select end time", binding.constraintDateRange)
                return false
            }
            searchEvent?.endDate!!.isEmpty() -> {
                longSnackBar("Please select end data", binding.constraintDateRange)
                return false
            }
            else -> return true
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }

    private fun showTimePickerDialog(timePickerTitle: String) {
        val timePicker = MaterialTimePicker.Builder()
            .setTheme(R.style.TimePickerTheme)
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(60)
            .setTitleText(timePickerTitle)
            .build()
        timePicker.isCancelable = false
        timePicker.addOnPositiveButtonClickListener {
            var timeState = "AM"
            if (timePicker.hour > 11) timeState = "PM"

            var hour = timePicker.hour.toString()
            if (timePicker.hour > 12) hour = (timePicker.hour -12).toString()
            var minute = timePicker.minute.toString()
            if (timePicker.hour.toString().length == 1) hour = "0$hour"
            if (minute.length == 1) minute = "0$minute"
            if (hour == "00") hour = "12"

            val selectedTime = "$hour:$minute $timeState"

            if (timePickerTitle == "Select Event Start Time"){
                searchEvent?.startTime = selectedTime
                binding.inputStartTime.setText(selectedTime)
            }else{
                searchEvent?.endTime = selectedTime
                binding.inputEndTime.setText(selectedTime)
            }
        }
        timePicker.addOnNegativeButtonClickListener {
            timePicker.dismiss()
        }
        timePicker.show(requireActivity().supportFragmentManager,
            context?.resources?.getString(R.string.time_picker))
    }

    private fun showDatePickerDialog(datePickerTitle: String) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.DatePickerTheme)
            .setCalendarConstraints(constraintsBuilder.build())
            .setTitleText(datePickerTitle)
            .build()

        datePicker.isCancelable = false
        datePicker.addOnPositiveButtonClickListener {
            if (datePickerTitle == "Select Event Start Date") {
                searchEvent?.startDate = Extensions.convertedDateFormat(datePicker.headerText)
                binding.inputStartDate.setText(datePicker.headerText)
            }else{
                searchEvent?.endDate = Extensions.convertedDateFormat(datePicker.headerText)
                binding.inputEndDate.setText(datePicker.headerText)
            }

        }

        datePicker.addOnNegativeButtonClickListener {
            datePicker.dismiss()
        }

        datePicker.show(requireActivity().supportFragmentManager,
            context?.resources?.getString(R.string.date_picker)
        )

    }

    private val constraintsBuilder =
        CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
}