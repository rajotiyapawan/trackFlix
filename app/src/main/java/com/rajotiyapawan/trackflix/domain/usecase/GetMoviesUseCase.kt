package com.rajotiyapawan.trackflix.domain.usecase

import com.rajotiyapawan.trackflix.domain.model.MovieCategory
import com.rajotiyapawan.trackflix.domain.repository.RemoteMovieRepository

class GetMoviesUseCase(private val repository: RemoteMovieRepository) {
    suspend operator fun invoke(category: MovieCategory) = repository.getMoviesByCategory(category)
}