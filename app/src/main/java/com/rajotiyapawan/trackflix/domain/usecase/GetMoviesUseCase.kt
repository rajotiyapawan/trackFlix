package com.rajotiyapawan.trackflix.domain.usecase

import com.rajotiyapawan.trackflix.domain.model.DiscoverMovieList
import com.rajotiyapawan.trackflix.domain.model.MovieCategory
import com.rajotiyapawan.trackflix.domain.repository.LocalMovieRepository
import com.rajotiyapawan.trackflix.domain.repository.RemoteMovieRepository
import com.rajotiyapawan.trackflix.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetMoviesUseCase(
    private val repository: RemoteMovieRepository,
    private val localMovieRepository: LocalMovieRepository,
    private val isNetworkAvailable: Boolean
) {
    operator fun invoke(category: MovieCategory): Flow<UiState<DiscoverMovieList>> = flow {
        emit(UiState.Loading)
        if (isNetworkAvailable) {
            when (val result = repository.getMoviesByCategory(category)) {
                is UiState.Success -> {
                    result.data.results?.let {
                        localMovieRepository.insertMoviesByCategory(category = category, it)
                    }
                    emit(UiState.Success(result.data))
                }

                is UiState.Error -> {
                    emit(UiState.Error(result.message))
                }

                else -> {}
            }
        } else {
            localMovieRepository.getMoviesByCategory(category).collect {
                if (it.isEmpty()) {
                    emit(UiState.Error("No internet and no cached data"))
                } else {
                    emit(
                        UiState.Success(
                            DiscoverMovieList(
                                page = null, total_pages = null, total_results = null, results = it
                            )
                        )
                    )
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}