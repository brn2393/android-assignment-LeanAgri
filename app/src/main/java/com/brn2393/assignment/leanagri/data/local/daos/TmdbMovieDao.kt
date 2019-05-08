package com.brn2393.assignment.leanagri.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.brn2393.assignment.leanagri.data.remote.models.TmdbMovie

@Dao
interface TmdbMovieDao {

    @Transaction
    @Query("SELECT * FROM tmdb_movies")
    fun getAll(): List<TmdbMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tmdbMovieEntity: TmdbMovie)

    @Delete
    fun delete(tmdbMovieEntity: TmdbMovie)

    @Query("DELETE FROM tmdb_movies")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM tmdb_movies WHERE movie_id = :id LIMIT 1")
    fun getMovieWith(id: String): LiveData<TmdbMovie>
}