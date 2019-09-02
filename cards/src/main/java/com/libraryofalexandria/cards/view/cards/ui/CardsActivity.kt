package com.libraryofalexandria.cards.view.cards.ui

import android.app.ActivityOptions
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.libraryofalexandria.cards.di.cardsModule
import com.libraryofalexandria.cards.view.R
import com.libraryofalexandria.cards.view.cards.CardAction
import com.libraryofalexandria.cards.view.cards.CardState
import com.libraryofalexandria.cards.view.cards.CardsViewModel
import com.libraryofalexandria.core.base.Activities
import com.libraryofalexandria.core.base.State
import com.libraryofalexandria.core.base.intentTo
import com.libraryofalexandria.core.extensions.observe
import com.libraryofalexandria.core.ui.InfiniteScrollListener
import kotlinx.android.synthetic.main.activity_cards.*
import kotlinx.android.synthetic.main.activity_cards.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CardsActivity : AppCompatActivity(),
    CardsAdapter.OnCardClickListener {

    private val loadFeature by lazy { loadKoinModules(arrayListOf(cardsModule)) }

    private fun injectFeature() = loadFeature

    private val viewModel by viewModel<CardsViewModel>()

    private val adapter = CardsAdapter(this)
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        injectFeature()
        initAdapter()
        observeState()
        viewModel.handleAction(CardAction.FirstLoad(intent.getStringExtra(Activities.Cards.expansion)))
    }

    private fun initAdapter() {
        recyclerView = recycler
        recyclerView.adapter = adapter

        val infiniteScrollListener =
            object : InfiniteScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                override fun isDataLoading(): Boolean = viewModel.state.value is CardState.Loading

                override fun onLoadMore() {
                    if (adapter.itemCount < intent.getIntExtra(Activities.Cards.total, adapter.itemCount))
                        viewModel.handleAction(CardAction.LoadMore)
                }
            }

        recyclerView.addOnScrollListener(infiniteScrollListener)
    }

    private fun observeState() = observe(viewModel.state, ::updateViewState)

    private fun updateViewState(state: State) = when (state) {
        is CardState.Loading -> progressBar.visibility = state.visibility
        is CardState.Loaded -> {
            progressBar.visibility = state.loadingVisibility
            showCards(state.cards)
        }
        is CardState.Error.Generic -> handleError(state)
        else -> throw Exception()
    }

    private fun handleError(state: CardState.Error.Generic) {
        Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
    }

    private fun showCards(sets: List<CardViewEntity>) {
        adapter.addAll(sets)
    }

    override fun onItemClick(cardViewEntity: CardViewEntity) {
        flavor.text = cardViewEntity.flavor
        mainText.text = cardViewEntity.plainText
        type.text = cardViewEntity.type
        manaCost.text = cardViewEntity.plainCost
        name.text = cardViewEntity.name
        rarity.text = cardViewEntity.rarity

        Glide.with(this)
            .load(cardViewEntity.artUrl)
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

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(arrayListOf(cardsModule))
    }
}
