package com.rajotiyapawan.trackflix.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rajotiyapawan.trackflix.FlixViewModel

@Composable
fun FavouritesScreen(modifier: Modifier = Modifier, viewModel: FlixViewModel) {
    val list = viewModel.favouritesList.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getFavourites()
    }
    Column(modifier.padding(horizontal = 16.dp)) {
        Text("Your Favourites", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        if (list.value.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("You do not have any Favourites.", fontSize = 30.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            }
        } else {
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(list.value) { movie ->
                    Row(
                        Modifier
                            .height(IntrinsicSize.Min)
                            .clickable {
                                viewModel.getMovieDetails(movie.id)
                                viewModel.sendUiEvent(UiEvent.Navigate("movieDetail"))
                            }) {
                        AsyncImage(
                            model = viewModel.posterPath + movie.poster_path,
                            modifier = Modifier
                                .height(250.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(movie.title ?: "", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(movie.overview ?: "", fontSize = 16.sp, fontWeight = FontWeight.Normal, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}