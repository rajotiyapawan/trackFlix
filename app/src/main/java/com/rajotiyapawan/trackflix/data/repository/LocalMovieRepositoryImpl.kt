package com.rajotiyapawan.trackflix.data.repository

import com.rajotiyapawan.trackflix.data.local.MovieCategoryCrossRef
import com.rajotiyapawan.trackflix.data.local.MovieDao
import com.rajotiyapawan.trackflix.data.mapper.toDomain
import com.rajotiyapawan.trackflix.data.mapper.toEntity
import com.rajotiyapawan.trackflix.data.mapper.toNowPlayingEntity
import com.rajotiyapawan.trackflix.domain.model.ConfigData
import com.rajotiyapawan.trackflix.domain.model.MovieCategory
import com.rajotiyapawan.trackflix.domain.model.MovieData
import com.rajotiyapawan.trackflix.domain.repository.LocalMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMovieRepositoryImpl(private val movieDao: MovieDao) : LocalMovieRepository {

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

    override suspend fun insertTrendingMovies(movies: List<MovieData>) {
        val movieEntities = movies.map { it.toEntity() }
        // Replace existing entries for this category
        movieDao.clearCategory(MovieCategory.TRENDING.name)
        val crossRefs = movies.mapIndexed { index, movie ->
            MovieCategoryCrossRef(
                movieId = movie.id ?: 0,
                category = MovieCategory.TRENDING.name,
                position = index
            )
        }
        movieDao.insertMovies(movieEntities)
        movieDao.insertCategoryRefs(crossRefs)
    }

    override fun getTrendingMovies(): Flow<List<MovieData>> {
        return movieDao.getTrendingMovies().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun insertNowPlayingMovies(movies: List<MovieData>) {
        movieDao.insertAllNowPlaying(movies.map { it.toNowPlayingEntity() })
    }

    override fun getNowPlayingMovies(): Flow<List<MovieData>> {
        return movieDao.getNowPlayingMovies().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun saveConfigData(data: ConfigData) {
        TODO("Not yet implemented")
    }

    override suspend fun getConfigData(data: ConfigData): ConfigData {
        TODO("Not yet implemented")
    }
}