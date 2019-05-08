package com.brn2393.assignment.leanagri.data

import android.content.Context
import android.util.Log
import com.brn2393.assignment.leanagri.R
import com.brn2393.assignment.leanagri.data.local.TmdbDatabase
import com.brn2393.assignment.leanagri.data.remote.RemoteAPI
import com.brn2393.assignment.leanagri.data.remote.models.AllMovieResponse
import com.brn2393.assignment.leanagri.data.remote.models.TmdbMovie
import io.reactivex.Observable

class RepositoryImpl(
    private val context: Context,
    private val remoteAPI: RemoteAPI,
    private val tmdbDatabase: TmdbDatabase
) : Repository {

    override fun fetchMovies(): Observable<AllMovieResponse> {
        return remoteAPI.fetchMovies(key = context.getString(R.string.tmdb_api_key))
    }

    override fun getMovies(): List<TmdbMovie> {
        val a = tmdbDatabase.tmdbMovieDao().getAll()
        Log.i("#", "size: ${a.size}")
        return a
    }

    override fun storeMovies(results: List<TmdbMovie>) {
        results.forEach {
            tmdbDatabase.tmdbMovieDao().insert(it)
        }
    }
}