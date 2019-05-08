package com.brn2393.assignment.leanagri.presentation.details

import android.annotation.SuppressLint
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brn2393.assignment.leanagri.data.Repository
import com.brn2393.assignment.leanagri.data.remote.models.AllMovieResponse
import com.brn2393.assignment.leanagri.data.remote.models.TmdbMovie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

class MovieDetailsViewModel : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    var apiErrorState: ObservableField<String> = ObservableField()
    var progressState: ObservableField<Boolean> = ObservableField(false)
    var apiDataAvailability: ObservableField<Boolean> = ObservableField(false)
    var moviesApiResponse: MutableLiveData<List<TmdbMovie>> = MutableLiveData()
    var repository: Repository? = null
    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun initFetchMovieDetails(intent: Intent) {
        apiDataAvailability.set(true)
        progressState.set(false)
/*        repository!!.fetchMovies()
            .doOnSubscribe {
                progressState.set(true)
                apiDataAvailability.set(true)
                compositeDisposable.add(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fetchDetailsSuccessConsumer, fetchDetailsErrorConsumer)*/
    }

    private val fetchDetailsSuccessConsumer = Consumer<AllMovieResponse> { response ->
        progressState.set(false)
        if (response.results.isNotEmpty()) {
            moviesApiResponse.value = response.results
            apiDataAvailability.set(true)
        } else {
            apiErrorState.set("response is empty.")
            apiDataAvailability.set(false)
        }
    }

    private val fetchDetailsErrorConsumer = Consumer<Throwable> { throwable ->
        progressState.set(false)
        apiErrorState.set(throwable.localizedMessage)
        apiDataAvailability.set(false)
    }

    fun clearDisposables() {
        if (!compositeDisposable.isDisposed) compositeDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        clearDisposables()
    }
}