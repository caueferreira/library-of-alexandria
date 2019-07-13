package com.libraryofalexandria.cards.view.expansions.ui

import android.app.ActivityOptions
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.libraryofalexandria.cards.di.cardsModule
import com.libraryofalexandria.cards.view.R
import com.libraryofalexandria.cards.view.expansions.ExpansiosViewModel
import com.libraryofalexandria.core.Activities
import com.libraryofalexandria.core.intentTo
import kotlinx.android.synthetic.main.activity_cards.progressBar
import kotlinx.android.synthetic.main.activity_expansions.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class ExpansionsActivity : AppCompatActivity(),
    ExpansionsAdapter.OnExpansionClickListener, FiltersAdapter.OnFilterClickListener {

    private val loadFeature by lazy { loadKoinModules(cardsModule) }

    private val viewModel by viewModel<ExpansiosViewModel>()

    private val adapter = ExpansionsAdapter(this)
    private val filterAdapter = FiltersAdapter(this)

    private lateinit var toolbarView: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var filtersView: RecyclerView
    private lateinit var drawerView: DrawerLayout

    private fun injectFeature() = loadFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expansions)

        injectFeature()
        initToolbar()
        initAdapter()
        initDrawer()
        observeState()
    }

    private fun initAdapter() {
        recyclerView = recycler
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.HORIZONTAL))
    }

    private fun initToolbar() {
        toolbarView = toolbar
        toolbarView.inflateMenu(R.menu.main)
        setActionBar(toolbarView)
    }

    private fun initDrawer() {
        filtersView = filters

        viewModel.filters.observe(this, Observer {
            drawerView = drawer

            filterAdapter.addAll(it)
            filtersView.adapter = filterAdapter
        })
    }

    private fun observeState() {
        viewModel.state.observe(this,
            Observer {
                renderState(it)
            }
        )
    }

    private fun renderState(viewState: ExpansionViewState) = with(viewState) {
        when (this) {
            is ExpansionViewState.Expansions.Loading -> progressBar.visibility = this.visibility
            is ExpansionViewState.Expansions.Error.Generic -> {
                progressBar.visibility = this.errorVisibility
                errorLayout.visibility = this.loadingVisibility
            }
            is ExpansionViewState.Expansions.Loaded -> {
                errorLayout.visibility = this.loadingVisibility
                progressBar.visibility = this.errorVisibility

                if (adapter.itemCount > 0) {
                    updateList.visibility = View.VISIBLE
                    updateList.setOnClickListener {
                        adapter.addAll(this.expansions)
                        recyclerView.scheduleLayoutAnimation()
                        updateList.visibility = View.GONE
                    }
                } else {
                    adapter.addAll(this.expansions)
                    recyclerView.scheduleLayoutAnimation()
                }
            }
        }
    }

    private fun filterExpansions(filter: FilterViewEntity) {
        adapter.filterBy(filter)
        recyclerView.scheduleLayoutAnimation()

        if (adapter.itemCount == 0) {
            emptyLayout.visibility = View.VISIBLE
        } else {
            emptyLayout.visibility = View.INVISIBLE
        }
    }

    override fun onItemClick(expansionViewEntity: ExpansionViewEntity) {
        startActivity(
            intentTo(Activities.Cards)
                .putExtra(Activities.Cards.expansion, expansionViewEntity.code)
                .putExtra(Activities.Cards.total, expansionViewEntity.totalCardsPlain)
        )
    }

    override fun onItemClick(viewEntity: FilterViewEntity) {
        filterExpansions(viewEntity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.about -> {
            startActivity(
                intentTo(Activities.About),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
            true
        }

        R.id.filter -> {
            if (drawerView.isDrawerOpen(GravityCompat.END))
                drawerView.closeDrawers()
            else
                drawerView.openDrawer(GravityCompat.END)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(cardsModule)
    }
}