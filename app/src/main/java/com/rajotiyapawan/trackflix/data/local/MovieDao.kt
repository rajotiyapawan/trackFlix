package com.rajotiyapawan.trackflix.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM favorite_movies")
    fun getAllFavorites(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(movie: MovieEntity)

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
