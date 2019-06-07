package com.caueferreira.cards.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.caueferreira.cards.di.cardsModule
import com.caueferreira.cards.view.State
import kotlinx.android.synthetic.main.activity_cards.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class CardsActivity : AppCompatActivity(), CardsAdapter.OnCardClickListener {

    private val viewModel by viewModel<CardsViewModel>()
    private val loadFeature by lazy { loadKoinModules(cardsModule) }

    private val adapter = CardsAdapter(this)
    private lateinit var recyclerView: RecyclerView

    private var itemCount = 0

    private fun injectFeature() = loadFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeature()
        setContentView(R.layout.activity_cards)

        initAdapter()
        observeState()
        observeCards()
    }

    private fun initAdapter() {
        itemCount = 1
        recyclerView = recycler
        val layoutManager = StaggeredGridLayoutManager(
            itemCount, StaggeredGridLayoutManager.HORIZONTAL
        )
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun observeState() {
        viewModel.state().observe(this,
            Observer {
                if(it == State.LOADING){
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                }
            }
        )
    }

    private fun observeCards() {
        viewModel.cards().observe(this,
            Observer { cards ->
                adapter.submitList(cards)
            }
        )
    }

    override fun onItemClick(card: CardViewEntity) {
        flavor.text = card.flavor
        mainText.text = card.plainText
        type.text = card.type
        manaCost.text = card.plainCost
        name.text = card.name
        rarity.text = card.rarity

        Glide.with(this)
            .load(card.artUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(croppedArt)
    }
}
