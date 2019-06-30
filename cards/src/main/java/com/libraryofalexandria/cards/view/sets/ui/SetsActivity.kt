package com.libraryofalexandria.cards.view.sets.ui

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
import com.libraryofalexandria.cards.view.State
import com.libraryofalexandria.cards.view.sets.SetsViewModel
import com.libraryofalexandria.core.Activities
import com.libraryofalexandria.core.intentTo
import com.libraryofalexandria.core.observe
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
        observeSets()

        viewModel.fetch()
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
        drawerView = drawer

        filterAdapter.addAll(
            arrayListOf(
                FilterViewEntity(
                    com.libraryofalexandria.R.drawable.ic_local_library,
                    android.R.color.holo_green_dark,
                    R.string.core,
                    SetFilter.CORE
                ),
                FilterViewEntity(
                    com.libraryofalexandria.R.drawable.ic_local_library,
                    android.R.color.holo_purple,
                    R.string.draft,
                    SetFilter.DRAFT
                ),
                FilterViewEntity(
                    com.libraryofalexandria.R.drawable.ic_local_library,
                    android.R.color.holo_orange_dark,
                    R.string.supplemental,
                    SetFilter.SUPPLEMENTAL
                ),
                FilterViewEntity(
                    com.libraryofalexandria.R.drawable.ic_local_library,
                    android.R.color.holo_blue_dark,
                    R.string.other,
                    SetFilter.OTHER
                )
            )
        )

        filtersView = filters
        filtersView.adapter = filterAdapter
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
        recyclerView.scheduleLayoutAnimation()
    }

    private fun filterSets(filter: SetFilter) {
        viewModel.filter(filter)
    }

    override fun onItemClick(setViewEntity: SetViewEntity) {
        startActivity(
            intentTo(Activities.Cards)
                .putExtra(Activities.Cards.set, setViewEntity.code)
                .putExtra(Activities.Cards.total, setViewEntity.totalCardsPlain)
        )
    }

    override fun onItemClick(viewEntity: FilterViewEntity) {
        filterSets(viewEntity.type)
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