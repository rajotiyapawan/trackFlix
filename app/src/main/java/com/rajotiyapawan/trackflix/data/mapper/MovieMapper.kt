package com.rajotiyapawan.trackflix.data.mapper

import com.rajotiyapawan.trackflix.data.local.MovieEntity
import com.rajotiyapawan.trackflix.domain.model.MovieData
import com.rajotiyapawan.trackflix.domain.model.getMovieData

fun MovieData.toEntity(): MovieEntity = MovieEntity(id = id!!, title = title!!, overview = overview!!, posterPath = poster_path!!)
fun MovieEntity.toDomain(): MovieData = getMovieData().copy(id = id, title = title, poster_path = posterPath, overview = overview)