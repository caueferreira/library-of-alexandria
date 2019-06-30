package com.libraryofalexandria.cards.view.sets.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_set.view.*

class SetsAdapter(
    private val listener: OnSetClickListener
) : RecyclerView.Adapter<SetsAdapter.SetViewHolder>() {

    var sets = arrayListOf<SetViewEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.libraryofalexandria.cards.view.R.layout.item_set, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        sets[position]?.let {
            holder.bind(it, listener)
            animate(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int = sets.size

    fun addAll(setViewEntityList: List<SetViewEntity>) {
        sets.clear()
        sets.addAll(setViewEntityList)
        notifyDataSetChanged()
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

    class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            viewEntity: SetViewEntity,
            listener: OnSetClickListener
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

    interface OnSetClickListener {
        fun onItemClick(viewEntity: SetViewEntity)
    }
}

