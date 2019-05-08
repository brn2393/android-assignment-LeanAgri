package com.brn2393.assignment.leanagri.presentation.details

import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.brn2393.assignment.leanagri.R
import com.brn2393.assignment.leanagri.data.remote.models.TmdbMovie
import com.brn2393.assignment.leanagri.databinding.ActivityMovieDetailsBinding
import com.brn2393.assignment.leanagri.domain.DomainConstants
import com.brn2393.assignment.leanagri.domain.base.BaseActivity

class MovieDetailsActivity : BaseActivity() {

    private val movieDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
    }
    private val binding: ActivityMovieDetailsBinding  by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_movie_details) as ActivityMovieDetailsBinding
    }

    override fun initiate() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val tmdbMovie: TmdbMovie = intent.getParcelableExtra(DomainConstants.EXTRAS_KEY_MOVIE)
        supportActionBar?.title = tmdbMovie.title
        movieDetailsViewModel.repository = repository
        binding.lifecycleOwner = this
        binding.movieDetailsViewModel = movieDetailsViewModel
        binding.tmdbMovie = tmdbMovie
        setObservers()
        movieDetailsViewModel.initFetchMovieDetails(intent)
    }

    private fun setObservers() {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
