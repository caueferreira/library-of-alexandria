package com.libraryofalexandria.cards.view.cards.ui

import android.app.ActivityOptions
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.libraryofalexandria.cards.view.R
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.cards.CardsViewModel
import com.libraryofalexandria.core.Activities
import com.libraryofalexandria.core.ui.InfiniteScrollListener
import com.libraryofalexandria.core.intentTo
import com.libraryofalexandria.core.extensions.observe
import kotlinx.android.synthetic.main.activity_cards.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardsActivity : AppCompatActivity(),
    CardsAdapter.OnCardClickListener {

    private val viewModel by viewModel<CardsViewModel>()

    private val adapter = CardsAdapter(this)
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        initAdapter()
        observeState()
        observeCards()
    }

    private fun initAdapter() {
        recyclerView = recycler
        recyclerView.adapter = adapter

        val infiniteScrollListener =
            object : InfiniteScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun isDataLoading(): Boolean = viewModel.state == State.LOADING

                override fun onLoadMore() {
                    if (adapter.itemCount < intent.getIntExtra(Activities.Cards.total, adapter.itemCount))
                        viewModel.fetch(intent.getStringExtra(Activities.Cards.set))
                }
            }

        recyclerView.addOnScrollListener(infiniteScrollListener)
    }

    private fun observeState() {
        viewModel.state.observe(this,
            Observer {
                if (it == State.LOADING) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                }
            }
        )
    }

    private fun observeCards() {
        observe(viewModel.cards, ::showCards)
        viewModel.fetch(intent.getStringExtra(Activities.Cards.set))
    }

    private fun showCards(sets: List<CardViewEntity>) {
        adapter.addAll(sets)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cards, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.about -> {
            startActivity(
                intentTo(Activities.About),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}
