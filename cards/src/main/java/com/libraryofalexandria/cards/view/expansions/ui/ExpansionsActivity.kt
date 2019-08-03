package com.libraryofalexandria.cards.view.expansions.ui

import android.app.ActivityOptions
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.libraryofalexandria.cards.di.apiModule
import com.libraryofalexandria.cards.di.expansionsModule
import com.libraryofalexandria.cards.di.filtersModule
import com.libraryofalexandria.cards.view.R
import com.libraryofalexandria.cards.view.expansions.ExpansionState
import com.libraryofalexandria.cards.view.expansions.ExpansionViewModel
import com.libraryofalexandria.core.base.Activities
import com.libraryofalexandria.core.base.State
import com.libraryofalexandria.core.base.intentTo
import com.libraryofalexandria.core.extensions.observe
import com.libraryofalexandria.core.extensions.toggle
import kotlinx.android.synthetic.main.activity_cards.progressBar
import kotlinx.android.synthetic.main.activity_expansions.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class ExpansionsActivity : AppCompatActivity(),
    ExpansionsAdapter.OnExpansionClickListener,
    FiltersAdapter.OnFilterClickListener {

    private val loadFeature by lazy { loadKoinModules(arrayListOf(apiModule, filtersModule, expansionsModule)) }

    private val viewModel by viewModel<ExpansionViewModel>()

    private val adapter = ExpansionsAdapter(this)
    private val filterAdapter = FiltersAdapter(this)

    private lateinit var toolbarView: Toolbar
    private lateinit var recyclerView: RecyclerView

    private fun injectFeature() = loadFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expansions)

        injectFeature()
        initToolbar()
        initAdapter()
        observeState()
    }

    private fun initToolbar() {
        toolbarView = toolbar
        toolbarView.inflateMenu(R.menu.main)
        setActionBar(toolbarView)
    }

    private fun initAdapter() {
        recyclerView = recycler
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.HORIZONTAL))
    }

    private fun observeState() = observe(viewModel.state, ::updateViewState)


    private fun updateViewState(state: State) = when (state) {
        is ExpansionState.Expansions -> when (state) {
            is ExpansionState.Expansions.Loading -> showLoading(state)
            is ExpansionState.Expansions.Error.Generic -> handleError(state)
            is ExpansionState.Expansions.Loaded -> showExpansions(state)
        }
        is ExpansionState.Filters -> when (state) {
            is ExpansionState.Filters.Loaded -> showFilters(state)
        }
        else -> throw Exception()
    }

    private fun showLoading(viewState: ExpansionState.Expansions.Loading) {
        progressBar.visibility = viewState.visibility
    }

    private fun handleError(viewState: ExpansionState.Expansions.Error.Generic) {
        progressBar.visibility = viewState.loadingVisibility
        errorLayout.visibility = viewState.errorVisibility
        Toast.makeText(this, viewState.message, Toast.LENGTH_LONG).show()
    }

    private fun showFilters(viewState: ExpansionState.Filters.Loaded) {
        filterAdapter.addAll(viewState.filters)
        filters.adapter = filterAdapter

    }

    private fun showExpansions(viewState: ExpansionState.Expansions.Loaded) {
        errorLayout.visibility = viewState.loadingVisibility
        progressBar.visibility = viewState.errorVisibility

        if (viewState.isUpdate) {
            updateList.visibility = View.VISIBLE
            updateList.setOnClickListener {
                updateList.visibility = View.GONE
                adapter.addAll(viewState.expansions)
                recyclerView.scheduleLayoutAnimation()
            }
        } else {
            adapter.addAll(viewState.expansions)
            recyclerView.scheduleLayoutAnimation()
        }
    }

    private fun filterExpansions(filter: FilterViewEntity) {
        adapter.filterBy(filter)
        recyclerView.scheduleLayoutAnimation()

        checkEmptyState()
    }

    private fun checkEmptyState() {
        if (adapter.itemCount == 0) {
            emptyLayout.visibility = View.VISIBLE
        } else {
            emptyLayout.visibility = View.GONE
        }
    }

    override fun onItemClick(expansionViewEntity: ExpansionViewEntity) {
        startActivity(
            intentTo(Activities.Cards)
                .putExtra(Activities.Cards.expansion, expansionViewEntity.code)
                .putExtra(Activities.Cards.total, expansionViewEntity.totalCardsPlain)
        )
    }

    override fun onItemClick(viewEntity: FilterViewEntity) = filterExpansions(viewEntity)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.about -> {
            startActivity(
                intentTo(Activities.About),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
            true
        }

        R.id.filter -> {
            drawer.toggle()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(arrayListOf(apiModule, filtersModule, expansionsModule))
    }
}