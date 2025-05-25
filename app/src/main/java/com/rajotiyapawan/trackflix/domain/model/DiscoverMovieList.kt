package com.rajotiyapawan.trackflix.domain.model

data class DiscoverMovieList(
    val page: Int?, val results: List<MovieData>?, val total_pages: Int?, val total_results: Int?
)

data class MovieData(
    val id: Int?,
    val adult: Boolean?,
    val backdrop_path: String?,
    val genre_ids: List<Int>?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?,
    val name: String?
)

fun getMovieData(): MovieData {
    return MovieData(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
}

fun MovieData.getPoster(configData: ConfigData?, index: Int = 2): String {
    val baseUrl = configData?.images?.secure_base_url + configData?.images?.poster_sizes?.getOrElse(index) { "w500" }
    return "$baseUrl${this.poster_path}"
}