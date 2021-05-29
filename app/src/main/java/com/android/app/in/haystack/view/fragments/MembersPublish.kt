package com.android.app.`in`.haystack.view.fragments

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.`in`.haystack.R
import com.android.app.`in`.haystack.databinding.FragmentMembersPublishBinding
import com.android.app.`in`.haystack.network.repository.Repository
import com.android.app.`in`.haystack.network.response.event.AllMembers
import com.android.app.`in`.haystack.network.response.event.Event
import com.android.app.`in`.haystack.network.response.event.EventCreated
import com.android.app.`in`.haystack.utils.AppConstants
import com.android.app.`in`.haystack.utils.AppConstants.ARG_SERIALIZABLE
import com.android.app.`in`.haystack.utils.AppConstants.MEMBER_EMAIL
import com.android.app.`in`.haystack.utils.AppConstants.MEMBER_NAME
import com.android.app.`in`.haystack.utils.AppConstants.MEMBER_PHONE
import com.android.app.`in`.haystack.utils.AppConstants.POSITION
import com.android.app.`in`.haystack.utils.AppConstants.STATUS
import com.android.app.`in`.haystack.utils.Extensions.showAlertDialog
import com.android.app.`in`.haystack.utils.Extensions.showErrorResponse
import com.android.app.`in`.haystack.view.activity.MainMenuActivity
import com.android.app.`in`.haystack.view.adapters.NewlyAddedMembersAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MembersPublish: Fragment(), NewlyAddedMembersAdapter.MembersClickEventListener {


    private lateinit var binding: FragmentMembersPublishBinding
    private lateinit var bottomSheet: BottomSheetDialog
    private lateinit var addedMembersAdapter: NewlyAddedMembersAdapter
    private var events: Event? = null
    private var listMembers = arrayListOf<AllMembers>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMembersPublishBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events = arguments?.getSerializable(AppConstants.ARG_SERIALIZABLE) as Event
        Log.e("TAG", "events: $events")

        listMembers.clear()
        if (events!!.allmembers.size > 0){
            listMembers.addAll(events!!.allmembers)
        }

        binding.toolbarMembersPublish.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbarMembersPublish.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.addMember -> {
                    val bundle = bundleOf(ARG_SERIALIZABLE to events)
                    findNavController().navigate(R.id.action_membersPublish_to_addMembersFragment, bundle)
                    true
                }
                else -> false
            }

        }

        binding.btnPublish.setOnClickListener {
            showBottomSheet()
            getEventLatLong()
            publishCreatedEvent()
        }

        addedMembersAdapter = NewlyAddedMembersAdapter()
        binding.recyclerMembers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addedMembersAdapter
            addedMembersAdapter.update(requireContext(), listMembers, this@MembersPublish)
        }
    }

    private fun getEventLatLong() {
        val geoCoder = Geocoder(requireContext())
        var listAddress = listOf<Address>()
        val locationName = events?.streetaddress + "," + events?.city + "," + events?.state +
                "," + events?.zipcode

        try {

            listAddress = geoCoder.getFromLocationName(locationName, 5)
            if (listAddress != null){
                val location = listAddress[0] as Address
                events?.latitude = location.latitude.toString()
                events?.longitude = location.longitude.toString()
            }

        }catch (e: Exception){e.printStackTrace()}
    }

    private fun publishCreatedEvent() {
        Log.e("TAG", "events:: $events")
        Repository.createNewEvent(events!!, requireContext()).enqueue(object : Callback<EventCreated>{
            override fun onResponse(call: Call<EventCreated>, response: Response<EventCreated>) {
                Log.e("TAG", "response: "+response.body())
                try {
                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){

                            showSuccessAlert("Event created", response.body()?.message!!)

                        }else{
                            showAlertDialog("Error Occurred!", requireContext(), response.body()?.message!!)
                        }
                    }

                }catch (e: Exception){e.printStackTrace()}
                hideBottomSheet()
            }

            override fun onFailure(call: Call<EventCreated>, t: Throwable) {
                Log.e("TAG", "error: "+t.localizedMessage)
                showErrorResponse(t, binding.constraintPublishEvent)
                hideBottomSheet()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }

    private fun showSuccessAlert(title: String, message: String) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok") { dialogInterface, i ->
                dialogInterface.dismiss()
                findNavController().navigate(R.id.action_membersPublish_to_eventCreated)
            }
            .create()
        if (dialog.window != null)
            dialog.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation

        dialog.show()
    }

    private fun showBottomSheet(){
        bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.authentication_progress_bottom_sheet,
                requireActivity().findViewById<ConstraintLayout>(R.id.bottom_sheet)
            )
        bottomSheet.setCancelable(false)
        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private  fun hideBottomSheet(){
        bottomSheet.hide()
    }

    override fun removeMember(position: Int) {
        listMembers.removeAt(position)
        events?.allmembers?.removeAt(position)
        addedMembersAdapter.notifyItemChanged(position)
    }

    override fun editMember(position: Int) {
        val bundle = bundleOf(
            STATUS to "2",
            ARG_SERIALIZABLE to events,
            POSITION to position
        )
        findNavController().navigate(R.id.action_membersPublish_to_editMember, bundle)
    }
}