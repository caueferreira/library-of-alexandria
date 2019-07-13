package com.libraryofalexandria.cards.view.expansions.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.libraryofalexandria.core.extensions.addOrRemove
import kotlinx.android.synthetic.main.item_expansion.view.*

class ExpansionsAdapter(
    private val listener: OnExpansionClickListener
) : RecyclerView.Adapter<ExpansionsAdapter.ExpansionViewHolder>() {

    private var stored = arrayListOf<ExpansionViewEntity>()
    private var expansions = arrayListOf<ExpansionViewEntity>()
    private var filters = arrayListOf<FilterViewEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpansionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.libraryofalexandria.cards.view.R.layout.item_expansion, parent, false)
        return ExpansionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpansionViewHolder, position: Int) {
        expansions[position]?.let {
            holder.bind(it, listener)
            animate(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int = expansions.size

    fun addAll(expansionViewEntityList: List<ExpansionViewEntity>) {
        if (stored.isEmpty()) {
            stored.addAll(expansionViewEntityList)
        }
        expansions.clear()
        expansions.addAll(stored.filterNot { filters.contains(it.filterViewEntity) })
        notifyDataSetChanged()
    }

    fun filterBy(filter: FilterViewEntity) {
        filters.addOrRemove(filter)
        addAll(stored.filterNot { filters.contains(it.filterViewEntity) })
    }

    private var lastPosition = -1

    private fun animate(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(
                viewToAnimate.context,
                com.libraryofalexandria.R.anim.item_animation_fall_down
            )
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class ExpansionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            viewEntity: ExpansionViewEntity,
            listener: OnExpansionClickListener
        ) {
            itemView.name.text = viewEntity.name
            itemView.code.text = viewEntity.code
            itemView.totalCards.text = viewEntity.totalCards

            itemView.name.setTextColor(itemView.context.getColor(viewEntity.textColor))
            itemView.code.setTextColor(itemView.context.getColor(viewEntity.textColor))
            itemView.totalCards.setTextColor(itemView.context.getColor(viewEntity.textColor))

            itemView.setBackgroundColor(itemView.context.getColor(viewEntity.backgroundColor))

            itemView.setOnClickListener { listener.onItemClick(viewEntity) }
        }
    }

    interface OnExpansionClickListener {
        fun onItemClick(viewEntity: ExpansionViewEntity)
    }
}

