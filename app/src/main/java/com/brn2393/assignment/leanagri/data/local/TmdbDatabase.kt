package com.brn2393.assignment.leanagri.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.brn2393.assignment.leanagri.data.local.daos.TmdbMovieDao
import com.brn2393.assignment.leanagri.data.remote.models.TmdbMovie

@Database(entities = [TmdbMovie::class], version = 1, exportSchema = false)
@TypeConverters(TmdbMovie.GenreIdsConverter::class)
abstract class TmdbDatabase : RoomDatabase() {

    abstract fun tmdbMovieDao(): TmdbMovieDao

    companion object {

        @Volatile
        private var INSTANCE: TmdbDatabase? = null

        fun getInstance(context: Context): TmdbDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): TmdbDatabase {
            return Room.databaseBuilder(context, TmdbDatabase::class.java, "tmdb_data.db")
                .allowMainThreadQueries() //remove in production
                .build()
        }
    }
}