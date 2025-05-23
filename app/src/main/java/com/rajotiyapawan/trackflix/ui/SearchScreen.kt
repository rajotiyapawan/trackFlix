package com.rajotiyapawan.trackflix.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rajotiyapawan.trackflix.FlixViewModel

@Composable
fun SearchScreen(modifier: Modifier = Modifier, viewModel: FlixViewModel) {
    LaunchedEffect(Unit) { viewModel.initializeSearch()}
    val query by viewModel.query.collectAsState()
    val results by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = viewModel::onQueryChanged,
            placeholder = { Text("Search...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(results) { result ->
                Text(result, modifier = Modifier.padding(8.dp))
            }
        }
    }
}