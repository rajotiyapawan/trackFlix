package com.rajotiyapawan.trackflix.domain.repository

import com.rajotiyapawan.trackflix.domain.model.ConfigData
import com.rajotiyapawan.trackflix.domain.model.MovieData
import kotlinx.coroutines.flow.Flow

interface LocalMovieRepository {
    suspend fun getAllFavourites(): Flow<List<MovieData>>
    suspend fun addToFavorites(movie: MovieData)
    suspend fun removeFromFavourites(movie: MovieData)
    suspend fun saveConfigData(data: ConfigData)
    suspend fun getConfigData(data: ConfigData): ConfigData
    suspend fun getIsMovieBookmarked(movieId: Int): Flow<Boolean>
    suspend fun insertTrendingMovies(movies: List<MovieData>)
    suspend fun insertNowPlayingMovies(movies: List<MovieData>)
    fun getTrendingMovies(): Flow<List<MovieData>>
    fun getNowPlayingMovies(): Flow<List<MovieData>>
}