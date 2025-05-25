package com.rajotiyapawan.trackflix.data.remote.api

const val movieDetailsUrl = "movie/<id>?language=en-US"
const val searchMovieUrl = "search/movie?query=<query>&include_adult=false&language=en-US&page=1"
const val nowPlayingMoviesUrl = "movie/now_playing?language=en-US&page=1"
const val trendingMoviesUrl = "trending/movie/day?language=en-US"
const val configsUrl = "configuration"