package com.libraryofalexandria.cards.view.sets.ui

import android.app.ActivityOptions
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.libraryofalexandria.cards.di.cardsModule
import com.libraryofalexandria.cards.view.R
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.sets.SetsViewModel
import com.libraryofalexandria.core.Activities
import com.libraryofalexandria.core.intentTo
import com.libraryofalexandria.core.observe
import kotlinx.android.synthetic.main.activity_cards.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules


class SetsActivity : AppCompatActivity(),
    SetsAdapter.OnSetClickListener {

    private val loadFeature by lazy { loadKoinModules(cardsModule) }

    private val viewModel by viewModel<SetsViewModel>()

    private val adapter = SetsAdapter(this)
    private lateinit var recyclerView: RecyclerView

    private var itemCount = 0

    private fun injectFeature() = loadFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sets)

        injectFeature()
        initAdapter()
        observeState()
        observeSets()

        viewModel.fetch()
    }

    private fun initAdapter() {
        itemCount = 2
        recyclerView = recycler
        val layoutManager = StaggeredGridLayoutManager(
            itemCount, StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.HORIZONTAL))
        recyclerView.scheduleLayoutAnimation()
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

    private fun observeSets() {
        observe(viewModel.sets, ::showSets)
    }

    private fun showSets(sets: List<SetViewEntity>) {
        adapter.addAll(sets)
    }

    override fun onItemClick(setViewEntity: SetViewEntity) {
        startActivity(
            intentTo(Activities.Cards)
                .putExtra(Activities.Cards.set, setViewEntity.code)
                .putExtra(Activities.Cards.total, setViewEntity.totalCards)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
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
