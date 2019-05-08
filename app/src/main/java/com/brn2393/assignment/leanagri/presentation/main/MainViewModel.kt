package com.brn2393.assignment.leanagri.presentation.main

import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brn2393.assignment.leanagri.data.Repository
import com.brn2393.assignment.leanagri.data.remote.models.AllMovieResponse
import com.brn2393.assignment.leanagri.data.remote.models.TmdbMovie
import com.brn2393.assignment.leanagri.domain.DomainConstants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    var repository: Repository? = null
    var apiErrorState: ObservableField<String> = ObservableField()
    var progressState: ObservableField<Boolean> = ObservableField(false)
    var apiDataAvailability: ObservableField<Boolean> = ObservableField(false)
    var selectedMovie: MutableLiveData<TmdbMovie> = MutableLiveData()
    var moviesApiResponse: MutableLiveData<List<TmdbMovie>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun initFetchMovies() {
        if (repository!!.getMovies().isNullOrEmpty()) {
            repository!!.fetchMovies()
                .doOnSubscribe {
                    progressState.set(true)
                    apiDataAvailability.set(true)
                    compositeDisposable.add(it)
                }
                .subscribeOn(Schedulers.io())
                .doOnNext { storeToRoom(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fetchMoviesSuccessConsumer, fetchMoviesErrorConsumer)
        } else {
            val movies = repository!!.getMovies()
            if (movies.isNotEmpty()) {
                moviesApiResponse.value = movies
                apiDataAvailability.set(true)
            } else {
                apiErrorState.set("response is empty.")
                apiDataAvailability.set(false)
            }
        }
    }

    private fun storeToRoom(it: AllMovieResponse?) {
        it?.let { repository!!.storeMovies(it.results) }
    }

    private val fetchMoviesSuccessConsumer = Consumer<AllMovieResponse> { response ->
        progressState.set(false)
        if (response.results.isNotEmpty()) {
            moviesApiResponse.value = response.results
            apiDataAvailability.set(true)
        } else {
            apiErrorState.set("response is empty.")
            apiDataAvailability.set(false)
        }
    }

    private val fetchMoviesErrorConsumer = Consumer<Throwable> { throwable ->
        progressState.set(false)
        apiErrorState.set(throwable.localizedMessage)
        apiDataAvailability.set(false)
    }

    fun onMovieSelect(tmdbMovie: TmdbMovie) {
        selectedMovie.value = tmdbMovie
    }

    private fun clearDisposables() {
        if (!compositeDisposable.isDisposed) compositeDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        clearDisposables()
    }

    fun sortMovies(sortBy: Int) {
        moviesApiResponse.value = moviesApiResponse.value?.sortedWith(
            when (sortBy) {
                DomainConstants.SORT_BY_DATE -> compareByDescending { it.releaseMillis() }
                DomainConstants.SORT_BY_RATING -> compareByDescending { it.voteAverage }
                else -> compareByDescending { it.releaseMillis() }
            }
        )
    }
}