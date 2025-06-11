package com.rajotiyapawan.trackflix.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String
)

data class MovieWithCategories(
    @Embedded val movie: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId",
        associateBy = Junction(MovieCategoryCrossRef::class)
    )
    val categories: List<String>
)

data class CategoryWithMovies(
    @Embedded val crossRef: MovieCategoryCrossRef,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "id"
    )
    val movie: MovieEntity
)

@Entity(
    tableName = "movie_category_cross_ref",
    primaryKeys = ["movieId", "category"]
)
data class MovieCategoryCrossRef(
    val movieId: Int,
    val category: String,
    val position: Int
)