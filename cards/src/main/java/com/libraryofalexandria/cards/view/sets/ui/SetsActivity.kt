package com.libraryofalexandria.cards.view.sets.ui

import android.app.ActivityOptions
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.libraryofalexandria.cards.di.cardsModule
import com.libraryofalexandria.cards.view.R
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.sets.SetsViewModel
import com.libraryofalexandria.core.Activities
import com.libraryofalexandria.core.intentTo
import com.libraryofalexandria.core.extensions.observe
import kotlinx.android.synthetic.main.activity_cards.progressBar
import kotlinx.android.synthetic.main.activity_sets.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class SetsActivity : AppCompatActivity(),
    SetsAdapter.OnSetClickListener, FiltersAdapter.OnSetClickListener {

    private val loadFeature by lazy { loadKoinModules(cardsModule) }

    private val viewModel by viewModel<SetsViewModel>()

    private val adapter = SetsAdapter(this)
    private val filterAdapter = FiltersAdapter(this)

    private lateinit var toolbarView: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var filtersView: RecyclerView
    private lateinit var drawerView: DrawerLayout

    private fun injectFeature() = loadFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sets)

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


    private fun renderState(viewState: SetsViewState) {
        progressBar.visibility = viewState.isLoading
        errorLayout.visibility = viewState.isError

        if (viewState.isUpdate && adapter.itemCount > 0) {
            updateList.visibility = View.VISIBLE
            updateList.setOnClickListener {
                showSets(viewState.sets)
                updateList.visibility = View.INVISIBLE
            }
        } else {
            showSets(viewState.sets)
        }

        viewState.throwable?.let {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()

            errorLayout.isClickable = true
            errorLayout.setOnClickListener {
                viewModel.load()
            }
        }
    }

    private fun showSets(sets: List<SetViewEntity>) {
        adapter.addAll(sets)
        recyclerView.scheduleLayoutAnimation()
    }

    private fun filterSets(filter: FilterViewEntity) {
        adapter.filterBy(filter)
        recyclerView.scheduleLayoutAnimation()

        if (adapter.itemCount == 0) {
            emptyLayout.visibility = View.VISIBLE
        } else {
            emptyLayout.visibility = View.INVISIBLE
        }
    }

    override fun onItemClick(setViewEntity: SetViewEntity) {
        startActivity(
            intentTo(Activities.Cards)
                .putExtra(Activities.Cards.set, setViewEntity.code)
                .putExtra(Activities.Cards.total, setViewEntity.totalCardsPlain)
        )
    }

    override fun onItemClick(viewEntity: FilterViewEntity) {
        filterSets(viewEntity)
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