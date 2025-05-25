package com.rajotiyapawan.trackflix.domain.repository

import com.rajotiyapawan.trackflix.domain.model.ConfigData
import com.rajotiyapawan.trackflix.domain.model.DiscoverMovieList
import com.rajotiyapawan.trackflix.domain.model.MovieCategory
import com.rajotiyapawan.trackflix.domain.model.MovieData
import com.rajotiyapawan.trackflix.utils.UiState
import kotlinx.coroutines.flow.Flow

interface RemoteMovieRepository {
    suspend fun getMoviesByCategory(category: MovieCategory): UiState<DiscoverMovieList>
    suspend fun getMovieDetails(id: Int?): UiState<MovieData>
    suspend fun searchMovie(query: String): Flow<UiState<DiscoverMovieList>>
    suspend fun getConfigData(): Flow<UiState<ConfigData>>
}