package com.rajotiyapawan.trackflix.data.repository

import com.rajotiyapawan.trackflix.data.local.MovieCategoryCrossRef
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
    override suspend fun insertMoviesByCategory(category: MovieCategory, movies: List<MovieData>) {
        val movieEntities = movies.map { it.toEntity() }
        // Replace existing entries for this category
        movieDao.clearCategory(category.name)
        val crossRefs = movies.mapIndexed { index, movie ->
            MovieCategoryCrossRef(
                movieId = movie.id ?: 0,
                category = category.name,
                position = index
            )
        }
        movieDao.insertMovies(movieEntities)
        movieDao.insertCategoryRefs(crossRefs)
    }

    override suspend fun getMoviesByCategory(category: MovieCategory): Flow<List<MovieData>> {
        return movieDao.getMoviesByCategory(category.name).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addToFavorites(movie: MovieData) {
        val movieEntity = movie.toEntity()
        movieDao.insertMovie(movieEntity)
        movieDao.insertCategoryRefs(
            listOf(
                MovieCategoryCrossRef(
                    movieId = movie.id ?: 0,
                    category = MovieCategory.FAVOURITE.name,
                    position = 1
                )
            )
        )
    }

    override suspend fun removeFromFavourites(movie: MovieData) {
        movie.id?.let { movieDao.removeFromFavorites(movieId = it) }
    }

    override suspend fun getIsMovieBookmarked(movieId: Int): Flow<Boolean> {
        return movieDao.isFavorite(movieId)
    }

    override suspend fun saveConfigData(data: ConfigData) {

    }

//    override suspend fun getConfigData(data: ConfigData): ConfigData {
//
//    }
}