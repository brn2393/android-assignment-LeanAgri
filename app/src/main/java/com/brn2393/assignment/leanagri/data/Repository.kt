package com.brn2393.assignment.leanagri.data

import com.brn2393.assignment.leanagri.data.remote.models.AllMovieResponse
import com.brn2393.assignment.leanagri.data.remote.models.TmdbMovie
import io.reactivex.Observable

interface Repository {

    fun fetchMovies(): Observable<AllMovieResponse>
    fun getMovies(): List<TmdbMovie>
    fun storeMovies(results: List<TmdbMovie>)
}