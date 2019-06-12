package com.libraryofalexandria.cards.view.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.libraryofalexandria.cards.view.R
import kotlinx.android.synthetic.main.item_card.view.*

class CardsAdapter(
    private val listener: OnCardClickListener
) : PagedListAdapter<CardViewEntity, CardsAdapter.CardViewHolder>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, listener) }
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            cardViewEntity: CardViewEntity,
            listener: OnCardClickListener
        ) {
            Glide.with(itemView)
                .load(cardViewEntity.cardUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.card)

            itemView.setOnClickListener { listener.onItemClick(cardViewEntity) }
        }
    }

    interface OnCardClickListener {
        fun onItemClick(cardViewEntity: CardViewEntity)
    }

    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         */
        private val diffCallback = object : DiffUtil.ItemCallback<CardViewEntity>() {
            override fun areItemsTheSame(oldItem: CardViewEntity, newItem: CardViewEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CardViewEntity, newItem: CardViewEntity): Boolean =
                oldItem == newItem
        }
    }
}

