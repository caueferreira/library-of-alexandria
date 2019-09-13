package com.libraryofalexandria.cards.view.cards.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.libraryofalexandria.cards.view.R
import kotlinx.android.synthetic.main.item_card.view.*

class CardsAdapter(
    private val listener: OnCardClickListener
) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    private var cards = arrayListOf<CardViewEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        cards[position].let { holder.bind(it, listener) }
    }

    override fun getItemCount() = cards.size

    fun addAll(setViewEntityList: List<CardViewEntity>) {
        cards.addAll(setViewEntityList)
        notifyDataSetChanged()
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
}