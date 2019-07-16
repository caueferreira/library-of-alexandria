package com.libraryofalexandria.cards.view.expansions.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.libraryofalexandria.cards.view.R
import kotlinx.android.synthetic.main.item_filter_expansion.view.*

class FiltersAdapter(
    private val listener: OnFilterClickListener
) : RecyclerView.Adapter<FiltersAdapter.FilterViewHolder>() {

    var filters = arrayListOf<FilterViewEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter_expansion, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        filters[position]?.let {
            holder.bind(it, listener)
        }
    }

    override fun getItemCount(): Int = filters.size

    fun addAll(viewEntityList: List<FilterViewEntity>) {
        filters.clear()
        filters.addAll(viewEntityList)
        notifyDataSetChanged()
    }

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            viewEntity: FilterViewEntity,
            listener: OnFilterClickListener
        ) {
            itemView.item.initialize(viewEntity)

            itemView.setOnClickListener {
                itemView.item.toggle()
                listener.onItemClick(viewEntity)
            }
        }
    }

    interface OnFilterClickListener {
        fun onItemClick(viewEntity: FilterViewEntity)
    }
}