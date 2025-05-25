package com.rajotiyapawan.trackflix.domain.usecase

import com.rajotiyapawan.trackflix.domain.repository.RemoteMovieRepository

class GetMovieDetailsUseCase(private val repository: RemoteMovieRepository) {
    suspend operator fun invoke(id: Int?) = repository.getMovieDetails(id)
}