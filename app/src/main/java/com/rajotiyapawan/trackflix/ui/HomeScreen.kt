package com.rajotiyapawan.trackflix.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rajotiyapawan.trackflix.DiscoverMovieList
import com.rajotiyapawan.trackflix.FlixViewModel
import com.rajotiyapawan.trackflix.MovieData

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: FlixViewModel) {
    LaunchedEffect(Unit) {
        viewModel.loadData()
        viewModel.loadNowPlayingData()
        viewModel.onQueryChanged("")
    }

    val context = LocalContext.current
    val trendingMoviesList = viewModel.trendingMovies.collectAsState()
    trendingMoviesList.value?.let { TrendingMovieView(modifier = modifier, data = it, viewModel = viewModel) }
}

@Composable
fun TrendingMovieView(modifier: Modifier = Modifier, data: DiscoverMovieList, viewModel: FlixViewModel) {
    Column(modifier) {
        Row {
            Box(
                Modifier
                    .padding(8.dp)
                    .padding(end = 8.dp)
                    .clickable {
                        viewModel.sendUiEvent(UiEvent.Navigate("search"))
                    }, contentAlignment = Alignment.CenterEnd
            ) {
                Row {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Text("Search")
                }
            }
            Spacer(Modifier.weight(1f))
            Box(
                Modifier
                    .padding(8.dp)
                    .padding(end = 8.dp)
                    .clickable {
                        viewModel.sendUiEvent(UiEvent.Navigate("favourites"))
                    }, contentAlignment = Alignment.CenterEnd
            ) {
                Row {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                    Text("Favourites")
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Trending Movies", modifier = Modifier.padding(start = 16.dp), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        LazyRow(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.results?.let {
                items(it) { movie ->
                    MovieItem(modifier = Modifier.clickable {
                        viewModel.getMovieDetails(movie.id)
                        viewModel.sendUiEvent(UiEvent.Navigate("movieDetail"))
                    }, movie, viewModel)
                }
            }
        }
        val nowPlayingMovies = viewModel.nowPlayingMovies.collectAsState()
        nowPlayingMovies.value?.let { nowPlaying ->
            Spacer(Modifier.height(16.dp))
            Text("Now Playing Movies", modifier = Modifier.padding(start = 16.dp), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                nowPlaying.results?.let {
                    items(it) { movie ->
                        MovieItem(modifier = Modifier.clickable {
                            viewModel.getMovieDetails(movie.id)
                            viewModel.sendUiEvent(UiEvent.Navigate("movieDetail"))
                        }, movie, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieItem(modifier: Modifier = Modifier, movie: MovieData, viewModel: FlixViewModel) {
    Column(
        modifier
            .width(150.dp)
            .height(300.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = viewModel.posterPath + movie.poster_path,
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )
        Text(movie.title ?: "", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}