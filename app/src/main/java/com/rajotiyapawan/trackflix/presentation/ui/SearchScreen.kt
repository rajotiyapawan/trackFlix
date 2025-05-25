package com.rajotiyapawan.trackflix.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rajotiyapawan.trackflix.FlixViewModel
import com.rajotiyapawan.trackflix.domain.model.getPoster
import com.rajotiyapawan.trackflix.utils.noRippleClick

@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: FlixViewModel) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            Modifier
                .noRippleClick { viewModel.sendUiEvent(UiEvent.BackBtnClicked) }
                .padding(top = 8.dp, end = 8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
        TextField(
            value = query,
            onValueChange = viewModel::onQueryChanged,
            placeholder = { Text("Search...") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(results) { result ->
                Row(Modifier.noRippleClick {
                    viewModel.getMovieDetails(result.id)
                    viewModel.sendUiEvent(UiEvent.Navigate("movieDetail"))
                }) {
                    AsyncImage(
                        model = result.getPoster(viewModel.configData), contentDescription = null, modifier = Modifier
                            .height(90.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("${result.title ?: ""} (${result.release_date})", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text(result.overview ?: "", maxLines = 3, overflow = TextOverflow.Ellipsis)
                    }
                }
                Spacer(Modifier.height(4.dp))
                HorizontalDivider(Modifier.fillMaxWidth(), color = Color.Gray)
            }
        }
    }
}