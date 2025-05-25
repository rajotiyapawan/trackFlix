package com.rajotiyapawan.trackflix.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trending_movies")
data class TrendingMovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String
)