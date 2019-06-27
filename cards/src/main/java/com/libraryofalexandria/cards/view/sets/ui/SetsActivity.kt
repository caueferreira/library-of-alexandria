package com.libraryofalexandria.cards.view.sets.ui

import android.app.ActivityOptions
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.libraryofalexandria.cards.di.cardsModule
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
    SetsAdapter.OnSetClickListener {

    private val loadFeature by lazy { loadKoinModules(cardsModule) }

    private val viewModel by viewModel<SetsViewModel>()

    private val adapter = SetsAdapter(this)

    private lateinit var recyclerView: RecyclerView
    private lateinit var navigationView: NavigationView
    private lateinit var drawerView: DrawerLayout

    private fun injectFeature() = loadFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.libraryofalexandria.cards.view.R.layout.activity_sets)

        injectFeature()
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

    private fun initDrawer() {
        drawerView = drawer

        navigationView = navigation
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                com.libraryofalexandria.cards.view.R.id.core -> filterSets(SetFilter.CORE)
                com.libraryofalexandria.cards.view.R.id.draft -> filterSets(SetFilter.DRAFT)
                com.libraryofalexandria.cards.view.R.id.constructed -> filterSets(SetFilter.CONSTRUCTED)
                com.libraryofalexandria.cards.view.R.id.other -> filterSets(SetFilter.OTHER)
            }
            true
        }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.libraryofalexandria.cards.view.R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        com.libraryofalexandria.cards.view.R.id.about -> {
            startActivity(
                intentTo(Activities.About),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
            true
        }

        com.libraryofalexandria.cards.view.R.id.filter -> {
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