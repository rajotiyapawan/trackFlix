package com.rajotiyapawan.trackflix.domain.usecase

import com.rajotiyapawan.trackflix.domain.repository.RemoteMovieRepository

class GetConfigDataUseCase(private val repository: RemoteMovieRepository) {
    suspend operator fun invoke() = repository.getConfigData()
}