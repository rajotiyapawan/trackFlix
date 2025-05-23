package com.rajotiyapawan.trackflix.ui

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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rajotiyapawan.trackflix.FlixViewModel

@Composable
fun MovieDetailView(modifier: Modifier = Modifier, viewModel: FlixViewModel) {
    val movieData by viewModel.selectedMovie.collectAsState()
    val configData = viewModel.configData
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearMovieDetails()
        }
    }
    if (movieData == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(Modifier.size(80.dp))
        }
    } else {
        Column(
            modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            AsyncImage(
                model = configData?.images?.secure_base_url + configData?.images?.poster_sizes?.get(3) + movieData?.poster_path,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillWidth
            )
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(movieData?.title ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Default.FavoriteBorder, contentDescription = null)
            }
            Spacer(Modifier.height(8.dp))
            Text(movieData?.overview ?: "", fontSize = 18.sp)
        }
    }
}