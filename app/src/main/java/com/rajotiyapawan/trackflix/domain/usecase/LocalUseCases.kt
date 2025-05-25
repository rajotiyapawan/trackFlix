package com.rajotiyapawan.trackflix.domain.usecase

import com.rajotiyapawan.trackflix.domain.model.MovieData
import com.rajotiyapawan.trackflix.domain.repository.LocalMovieRepository
import kotlinx.coroutines.flow.Flow

class AddToFavouriteUseCase(private val repository: LocalMovieRepository) {
    suspend operator fun invoke(movie: MovieData) = repository.addToFavorites(movie)
}

class RemoveFromFavoritesUseCase(private val repo: LocalMovieRepository) {
    suspend operator fun invoke(movie: MovieData) = repo.removeFromFavourites(movie)
}

class GetFavoriteMoviesUseCase(private val repo: LocalMovieRepository) {
    suspend operator fun invoke(): Flow<List<MovieData>> = repo.getAllFavourites()
}

class GetIsMovieBookmarkedUseCase(private val repo: LocalMovieRepository) {
    suspend operator fun invoke(movieId: Int): Flow<Boolean> = repo.getIsMovieBookmarked(movieId)
}