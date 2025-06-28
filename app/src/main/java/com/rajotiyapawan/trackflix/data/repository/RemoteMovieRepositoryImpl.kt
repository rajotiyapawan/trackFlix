package com.rajotiyapawan.trackflix.data.repository

import com.rajotiyapawan.trackflix.data.remote.api.configsUrl
import com.rajotiyapawan.trackflix.data.remote.api.movieDetailsUrl
import com.rajotiyapawan.trackflix.data.remote.api.nowPlayingMoviesUrl
import com.rajotiyapawan.trackflix.data.remote.api.searchMovieUrl
import com.rajotiyapawan.trackflix.data.remote.api.trendingMoviesUrl
import com.rajotiyapawan.trackflix.domain.model.ConfigData
import com.rajotiyapawan.trackflix.domain.model.DiscoverMovieList
import com.rajotiyapawan.trackflix.domain.model.MovieCategory
import com.rajotiyapawan.trackflix.domain.model.MovieData
import com.rajotiyapawan.trackflix.domain.repository.RemoteMovieRepository
import com.rajotiyapawan.trackflix.network.ApiResponse
import com.rajotiyapawan.trackflix.network.NetworkRepository
import com.rajotiyapawan.trackflix.network.TMDB_BaseUrl
import com.rajotiyapawan.trackflix.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteMovieRepositoryImpl : RemoteMovieRepository {
    override suspend fun getMoviesByCategory(category: MovieCategory): UiState<DiscoverMovieList> = try {
        val url = TMDB_BaseUrl + getUrlPath(category)
        when (val response = NetworkRepository.get<DiscoverMovieList>(url)) {
            is ApiResponse.Error -> UiState.Error(message = response.message, code = response.code)
            is ApiResponse.Success<DiscoverMovieList> -> {
                UiState.Success(response.data)
            }
        }
    } catch (e: Exception) {
        UiState.Error(e.localizedMessage ?: "Unknown error")
    }

    override suspend fun getMovieDetails(id: Int?): UiState<MovieData> = try {
        var url = TMDB_BaseUrl + movieDetailsUrl
        url = url.replace("<id>", id.toString())
        when (val response = NetworkRepository.get<MovieData>(url)) {
            is ApiResponse.Error -> UiState.Error(message = response.message, code = response.code)
            is ApiResponse.Success<MovieData> -> {
                UiState.Success(response.data)
            }
        }
    } catch (e: Exception) {
        UiState.Error(e.localizedMessage ?: "Unknown error")
    }

    override suspend fun searchMovie(query: String): Flow<UiState<DiscoverMovieList>> = flow {
        emit(UiState.Loading)
        var url = TMDB_BaseUrl + searchMovieUrl
        url = url.replace("<query>", query)
        when (val response = NetworkRepository.get<DiscoverMovieList>(url)) {
            is ApiResponse.Error -> emit(UiState.Error(message = response.message, code = response.code))
            is ApiResponse.Success<DiscoverMovieList> -> {
                val result = response.data
                emit(UiState.Success(result))
            }
        }
    }

    override suspend fun getConfigData(): Flow<UiState<ConfigData>> = flow {
        val url = TMDB_BaseUrl + configsUrl
        when (val response = NetworkRepository.get<ConfigData>(url)) {
            is ApiResponse.Error -> emit(UiState.Error(message = response.message, code = response.code))
            is ApiResponse.Success<ConfigData> -> {
                val result = response.data
                emit(UiState.Success(result))
            }
        }
    }

    private fun getUrlPath(category: MovieCategory): String {
        return when (category) {
            MovieCategory.TRENDING -> trendingMoviesUrl
            MovieCategory.NOW_PLAYING -> nowPlayingMoviesUrl
            MovieCategory.FAVOURITE -> "" // only for local use
        }
    }
}