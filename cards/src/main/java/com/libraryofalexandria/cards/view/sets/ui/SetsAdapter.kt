package com.libraryofalexandria.cards.view.sets.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_set.view.*


class SetsAdapter(
    private val listener: OnSetClickListener
) : PagedListAdapter<SetViewEntity, SetsAdapter.SetViewHolder>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.libraryofalexandria.cards.view.R.layout.item_set, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, listener) }
    }

    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            setViewEntity: SetViewEntity,
            listener: OnSetClickListener
        ) {
            itemView.name.text = setViewEntity.name
            itemView.code.text = setViewEntity.code
            itemView.totalCards.text = setViewEntity.totalCards

            itemView.name.setTextColor(itemView.context.getColor(setViewEntity.textColor))
            itemView.code.setTextColor(itemView.context.getColor(setViewEntity.textColor))
            itemView.totalCards.setTextColor(itemView.context.getColor(setViewEntity.textColor))

            itemView.setBackgroundColor(itemView.context.getColor(setViewEntity.backgroundColor))

            itemView.setOnClickListener { listener.onItemClick(setViewEntity) }
        }
    }

    interface OnSetClickListener {
        fun onItemClick(setViewEntity: SetViewEntity)
    }

    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         */
        private val diffCallback = object : DiffUtil.ItemCallback<SetViewEntity>() {
            override fun areItemsTheSame(oldItem: SetViewEntity, newItem: SetViewEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SetViewEntity, newItem: SetViewEntity): Boolean =
                oldItem == newItem
        }
    }
}

