package com.rajotiyapawan.trackflix.data.repository

import com.rajotiyapawan.trackflix.data.local.MovieDao
import com.rajotiyapawan.trackflix.data.mapper.toDomain
import com.rajotiyapawan.trackflix.data.mapper.toEntity
import com.rajotiyapawan.trackflix.domain.model.ConfigData
import com.rajotiyapawan.trackflix.domain.model.MovieCategory
import com.rajotiyapawan.trackflix.domain.model.MovieData
import com.rajotiyapawan.trackflix.domain.repository.LocalMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMovieRepositoryImpl(private val movieDao: MovieDao) : LocalMovieRepository {
    override suspend fun getMoviesByCategory(category: MovieCategory): Flow<List<MovieData>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFavourites(): Flow<List<MovieData>> {
        return movieDao.getAllFavorites().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addToFavorites(movie: MovieData) {
        movieDao.addToFavorites(movie.toEntity())
    }

    override suspend fun removeFromFavourites(movie: MovieData) {
        movieDao.removeFromFavorites(movie.toEntity())
    }

    override suspend fun getIsMovieBookmarked(movieId: Int): Flow<Boolean> {
        return movieDao.isMovieBookmarked(movieId)
    }

    override suspend fun saveConfigData(data: ConfigData) {
        TODO("Not yet implemented")
    }

    override suspend fun getConfigData(data: ConfigData): ConfigData {
        TODO("Not yet implemented")
    }
}