package com.libraryofalexandria.cards.view.sets.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.libraryofalexandria.cards.view.R
import kotlinx.android.synthetic.main.item_filter_set.view.*

class FiltersAdapter(
    private val listener: OnSetClickListener
) : RecyclerView.Adapter<FiltersAdapter.FilterViewHolder>() {

    var filters = arrayListOf<FilterViewEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter_set, parent, false)
        return FilterViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        filters[position]?.let {
            holder.bind(it, listener)
        }
    }

    override fun getItemCount(): Int = filters.size

    fun addAll(setViewEntityList: List<FilterViewEntity>) {
        filters.clear()
        filters.addAll(setViewEntityList)
        notifyDataSetChanged()
    }

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            viewEntity: FilterViewEntity,
            listener: OnSetClickListener
        ) {
            itemView.filterIcon.setImageDrawable(itemView.context.getDrawable(viewEntity.icon))
            itemView.filterIcon.setColorFilter(
                ContextCompat.getColor(itemView.context, viewEntity.iconColor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            itemView.filterText.text = itemView.context.getString(viewEntity.text)

            itemView.setOnClickListener {
                itemView.filterText.toggle()
                listener.onItemClick(viewEntity)
            }
        }
    }

    interface OnSetClickListener {
        fun onItemClick(viewEntity: FilterViewEntity)
    }
}