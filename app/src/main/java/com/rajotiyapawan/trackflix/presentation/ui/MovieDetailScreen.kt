package com.rajotiyapawan.trackflix.presentation.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rajotiyapawan.trackflix.FlixViewModel
import com.rajotiyapawan.trackflix.domain.model.MovieData
import com.rajotiyapawan.trackflix.domain.model.getPoster
import com.rajotiyapawan.trackflix.utils.ImageFromUrl
import com.rajotiyapawan.trackflix.utils.UiState
import com.rajotiyapawan.trackflix.utils.noRippleClick

@Composable
fun MovieDetailView(modifier: Modifier = Modifier, viewModel: FlixViewModel) {
    val movieData by viewModel.selectedMovie.collectAsState()
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearMovieDetails()
        }
    }
    when (val result = movieData) {
        is UiState.Error -> {
            Toast.makeText(LocalContext.current, "No details found", Toast.LENGTH_SHORT).show()
            viewModel.sendUiEvent(UiEvent.BackBtnClicked)
        }

        UiState.Idle -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(Modifier.align(Alignment.TopStart)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                Text("No Details Found", textAlign = TextAlign.Center, fontSize = 24.sp)
            }
        }

        UiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(Modifier.size(80.dp))
            }
        }

        is UiState.Success<MovieData> -> {
            LoadMovieDetails(modifier, result.data, viewModel)
        }
    }
}

@Composable
private fun LoadMovieDetails(modifier: Modifier = Modifier, movieData: MovieData, viewModel: FlixViewModel) {
    val uiMovieData: MovieData = remember { movieData }
    val isBookmarked = viewModel.isBookmarked.collectAsState()
    val context = LocalContext.current
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Box(
            Modifier
                .noRippleClick { viewModel.sendUiEvent(UiEvent.BackBtnClicked) }
                .padding(top = 8.dp, end = 8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
        Spacer(Modifier.height(12.dp))
        ImageFromUrl(
            imageUrl = uiMovieData.getPoster(viewModel.configData, index = 3),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.FillWidth
        )
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(uiMovieData.title ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(16.dp))
            Box(Modifier.noRippleClick { viewModel.toggleFavourite(uiMovieData) }) {
                Icon(if (isBookmarked.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = null)
            }
            Spacer(Modifier.width(16.dp))
            IconButton(onClick = {
                uiMovieData.let {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out this movie: ${it.title}\nhttps://rajotiyapawan.com/movie/${it.id}")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share Movie")
                    context.startActivity(shareIntent)
                }
            }) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(uiMovieData.overview ?: "", fontSize = 18.sp)
    }
}