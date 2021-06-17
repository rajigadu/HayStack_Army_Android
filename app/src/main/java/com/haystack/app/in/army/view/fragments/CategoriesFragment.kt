package com.haystack.app.`in`.army.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.haystack.app.`in`.army.R
import com.haystack.app.`in`.army.databinding.FragmentCategoriesBinding
import com.haystack.app.`in`.army.network.repository.Repository
import com.haystack.app.`in`.army.network.response.categories.AllCategories
import com.haystack.app.`in`.army.network.response.categories.Data
import com.haystack.app.`in`.army.network.response.event.Event
import com.haystack.app.`in`.army.network.response.search_events.SearchByEvent
import com.haystack.app.`in`.army.utils.AppConstants.ARG_OBJECTS
import com.haystack.app.`in`.army.utils.AppConstants.ARG_SERIALIZABLE
import com.haystack.app.`in`.army.utils.Extensions.showSnackBar
import com.haystack.app.`in`.army.view.activity.MainMenuActivity
import com.haystack.app.`in`.army.view.adapters.CategoriesListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesFragment: Fragment() {


    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesListAdapter: CategoriesListAdapter
    private var events: Event?= null
    private var navigation: String?= null
    private var listCategories = arrayListOf<Data>()
    private var searchEvent: SearchByEvent? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigation = arguments?.getString(ARG_OBJECTS)
        if (navigation == "1") {
            events = arguments?.getSerializable(ARG_SERIALIZABLE) as Event
            Log.e("TAG", "events: $events")
        }

        binding.refreshCategories.setColorSchemeColors(ContextCompat.getColor(requireContext(),
            R.color.colorPrimary))
        binding.refreshCategories.setOnRefreshListener {
            listCategories.clear()
            getCategories()
        }

        getCategories()

        categoriesListAdapter = CategoriesListAdapter(requireContext())
        binding.recyclerCategories.apply {
            adapter = categoriesListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.toolbarCategories.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnContinue.setOnClickListener {
            val selectedCategories = categoriesListAdapter.getSelectedCategories()
            searchEvent = SearchByEvent()
            if (selectedCategories.isNotEmpty()) {
                for (elements in selectedCategories) Log.e("TAG", "elements: $elements")
                val categories = selectedCategories.joinToString(",")
                events?.category = categories
                searchEvent?.category = categories
            }/*else {
                showSnackBar(binding.constraintCategories, "Please select categories")
                return@setOnClickListener
            }*/

            if (navigation == "1") {
                val bundle = bundleOf(ARG_SERIALIZABLE to events)
                findNavController().navigate(
                    R.id.action_categoriesFragment_to_createEventMode,
                    bundle
                )
            }else{
                val bundle = bundleOf(ARG_SERIALIZABLE to searchEvent)
                findNavController().navigate(R.id.action_categoriesFragment_to_searchFragment, bundle)
            }
        }
    }

    private fun getCategories() {
        binding.refreshCategories.isRefreshing = true
        Repository.getAllCategories().enqueue(object : Callback<AllCategories>{
            override fun onResponse(call: Call<AllCategories>, response: Response<AllCategories>) {
                try{

                    if (response.isSuccessful){
                        if (response.body()?.status == "1"){

                            if (response.body()?.data?.size!! > 0){
                                listCategories.clear()
                                listCategories.addAll(response.body()?.data!!)
                                categoriesListAdapter.update(listCategories)
                            }

                        }else{
                            showSnackBar(binding.constraintCategories, response.body()?.message!!)
                        }
                    }

                }catch (e: Exception){e.printStackTrace()}

                binding.refreshCategories.isRefreshing = false
            }

            override fun onFailure(call: Call<AllCategories>, t: Throwable) {
                try {
                    showSnackBar(binding.constraintCategories, "Something went wrong")
                    binding.refreshCategories.isRefreshing = false
                }catch (e: Exception){e.printStackTrace()}
            }

        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainMenuActivity).hideBottomNav()
    }
}