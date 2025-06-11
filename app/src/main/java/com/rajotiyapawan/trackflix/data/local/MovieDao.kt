package com.rajotiyapawan.trackflix.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM favorite_movies")
    fun getAllFavorites(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryRefs(refs: List<MovieCategoryCrossRef>)

    @Transaction
    @Query("SELECT * FROM favorite_movies INNER JOIN movie_category_cross_ref ON id = movieId WHERE category = :category ORDER BY position")
    fun getMoviesByCategory(category: String): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT * FROM movie_category_cross_ref WHERE movieId = :movieId AND category = 'favorite')")
    fun isFavorite(movieId: Int): Flow<Boolean>

    @Query("DELETE FROM movie_category_cross_ref WHERE category = :category")
    suspend fun clearCategory(category: String)

    @Query("DELETE FROM movie_category_cross_ref WHERE movieId = :movieId AND category = 'favorite'")
    suspend fun removeFromFavorites(movieId: Int)

    @Delete
    suspend fun removeFromFavorites(movie: MovieEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    fun isMovieBookmarked(movieId: Int): Flow<Boolean>

    @Query("SELECT * FROM trending_movies")
    fun getTrendingMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM now_playing_movies")
    fun getNowPlayingMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTrending(movies: List<TrendingMovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNowPlaying(movies: List<NowPlayingMovieEntity>)
}
