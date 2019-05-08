package com.brn2393.assignment.leanagri.data.remote.models

import android.os.Parcelable
import androidx.room.*
import com.brn2393.assignment.leanagri.data.remote.factories.RetrofitFactory
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "tmdb_movies")
@Parcelize
data class TmdbMovie(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "movie_id")
    @JsonProperty("id")
    val id: Int,
    @ColumnInfo(name = "vote_count")
    @JsonProperty("vote_count")
    val voteCount: Int,
    @ColumnInfo(name = "video")
    @JsonProperty("video")
    val video: Boolean,
    @ColumnInfo(name = "vote_average")
    @JsonProperty("vote_average")
    val voteAverage: Int,
    @ColumnInfo(name = "title")
    @JsonProperty("title")
    val title: String,
    @ColumnInfo(name = "popularity")
    @JsonProperty("popularity")
    val popularity: Double,
    @ColumnInfo(name = "poster_path")
    @JsonProperty("poster_path")
    val posterPath: String,
    @ColumnInfo(name = "original_language")
    @JsonProperty("original_language")
    val originalLanguage: String,
    @ColumnInfo(name = "original_title")
    @JsonProperty("original_title")
    val originalTitle: String,
    @TypeConverters(GenreIdsConverter::class)
    @ColumnInfo(name = "genre_ids")
    @JsonProperty("genre_ids")
    val genreIds: List<Int>,
    @ColumnInfo(name = "backdrop_path")
    @JsonProperty("backdrop_path")
    val backdropPath: String,
    @ColumnInfo(name = "adult")
    @JsonProperty("adult")
    val adult: Boolean,
    @ColumnInfo(name = "overview")
    @JsonProperty("overview")
    val overview: String,
    @ColumnInfo(name = "release_date")
    @JsonProperty("release_date")
    val releaseDate: String
) : Parcelable {
    override fun toString(): String {
        return "TmdbMovie(voteCount=$voteCount, id=$id, video=$video, voteAverage=$voteAverage, title='$title', popularity=$popularity, posterPath='$posterPath', originalLanguage='$originalLanguage', originalTitle='$originalTitle', genreIds=$genreIds, backdropPath='$backdropPath', adult=$adult, overview='$overview', releaseDate='$releaseDate')"
    }

    fun releaseMillis(): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return simpleDateFormat.parse(releaseDate).time
    }

    object GenreIdsConverter {

        @TypeConverter
        @JvmStatic
        fun toGenreIdList(inputString: String?): List<Int>? {
            if (inputString == null) return emptyList()
            val mapper = RetrofitFactory.objectMapper
            return mapper.readValue(inputString, object : TypeReference<List<Int>>() {})
        }

        @TypeConverter
        @JvmStatic
        fun toGenreIdString(list: List<Int>?): String? {
            if (list == null) return null
            val mapper = RetrofitFactory.objectMapper
            return mapper.writeValueAsString(list)
        }
    }
}