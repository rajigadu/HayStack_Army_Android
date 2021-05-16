package com.android.app.`in`.haystack.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.app.`in`.haystack.databinding.LayoutCategoriesListItemBinding


class CategoriesListAdapter(val context: Context)
    : RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: LayoutCategoriesListItemBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesListAdapter.ViewHolder =
        ViewHolder(LayoutCategoriesListItemBinding.inflate(LayoutInflater.from(
            parent.context
        ), parent, false))

    override fun onBindViewHolder(holder: CategoriesListAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 20
}