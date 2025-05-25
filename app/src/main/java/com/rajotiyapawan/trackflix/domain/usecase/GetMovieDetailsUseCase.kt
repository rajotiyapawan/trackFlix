package com.rajotiyapawan.trackflix.domain.usecase

import com.rajotiyapawan.trackflix.domain.model.MovieData
import com.rajotiyapawan.trackflix.domain.repository.RemoteMovieRepository
import com.rajotiyapawan.trackflix.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetMovieDetailsUseCase(
    private val repository: RemoteMovieRepository,
    private val isNetworkAvailable: Boolean
) {
    operator fun invoke(movie: MovieData): Flow<UiState<MovieData>> = flow {
        emit(UiState.Loading)
        if (isNetworkAvailable) {
            when (val result = repository.getMovieDetails(movie.id)) {
                is UiState.Error -> {
                    emit(UiState.Error(result.message))
                }

                is UiState.Success<MovieData> -> {
                    emit(UiState.Success(result.data))
                }

                else -> {}
            }
        } else {
            emit(UiState.Success(movie))
        }
    }.flowOn(Dispatchers.IO)
}