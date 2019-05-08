package com.brn2393.assignment.leanagri.data.remote.models

import com.fasterxml.jackson.annotation.JsonProperty

data class AllMovieResponse(
    @JsonProperty("page")
    val page: Int,
    @JsonProperty("results")
    val results: List<TmdbMovie>,
    @JsonProperty("total_pages")
    val totalPages: Int,
    @JsonProperty("total_results")
    val totalResults: Int
)