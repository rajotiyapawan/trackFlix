package com.rajotiyapawan.trackflix.domain.repository

import com.rajotiyapawan.trackflix.domain.model.ConfigData
import com.rajotiyapawan.trackflix.domain.model.MovieCategory
import com.rajotiyapawan.trackflix.domain.model.MovieData
import kotlinx.coroutines.flow.Flow

interface LocalMovieRepository {
    suspend fun insertMoviesByCategory(category: MovieCategory, movies: List<MovieData>)
    suspend fun getMoviesByCategory(category: MovieCategory): Flow<List<MovieData>>
    suspend fun addToFavorites(movie: MovieData)
    suspend fun removeFromFavourites(movie: MovieData)
    suspend fun saveConfigData(data: ConfigData)

    //    suspend fun getConfigData(data: ConfigData): ConfigData
    suspend fun getIsMovieBookmarked(movieId: Int): Flow<Boolean>
}