package com.brn2393.assignment.leanagri.data.remote

import com.brn2393.assignment.leanagri.data.remote.models.AllMovieResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteAPI {

    @GET("discover/movie")
    fun fetchMovies(
        @Query("api_key") key: String,
        @Query("language") language: String = "en-US",
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_video") includeVideo: String = "false",
        @Query("page") page: String = "1"
    ): Observable<AllMovieResponse>
}