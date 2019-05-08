package com.brn2393.assignment.leanagri.presentation.main

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.brn2393.assignment.leanagri.R
import com.brn2393.assignment.leanagri.databinding.ActivityMainBinding
import com.brn2393.assignment.leanagri.domain.DomainConstants
import com.brn2393.assignment.leanagri.domain.base.BaseActivity
import com.brn2393.assignment.leanagri.presentation.details.MovieDetailsActivity

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
    private val binding: ActivityMainBinding  by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
    }
    private lateinit var moviesListAdapter: MoviesListAdapter

    override fun initiate() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
        moviesListAdapter = MoviesListAdapter()
        mainViewModel.repository = repository
        moviesListAdapter.mainViewModel = mainViewModel
        binding.adapter = moviesListAdapter
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setObservers()
        mainViewModel.initFetchMovies()
    }

    private fun setObservers() {
        mainViewModel.moviesApiResponse.observe(this, Observer {
            moviesListAdapter.movies = it
        })
        mainViewModel.selectedMovie.observe(this, Observer {
            Intent(this, MovieDetailsActivity::class.java).apply {
                putExtra(DomainConstants.EXTRAS_KEY_MOVIE, it)
                startActivity(this)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when (item?.itemId) {
            R.id.overflow_sort_by_date -> mainViewModel.sortMovies(DomainConstants.SORT_BY_DATE)
            R.id.overflow_sort_by_rating -> mainViewModel.sortMovies(DomainConstants.SORT_BY_RATING)
        }
        return true
    }
}
