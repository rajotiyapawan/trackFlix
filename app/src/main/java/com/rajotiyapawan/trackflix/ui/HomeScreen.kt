package com.rajotiyapawan.trackflix.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rajotiyapawan.trackflix.FlixViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: FlixViewModel) {
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Column(modifier) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(end = 8.dp)
                .clickable {
                    viewModel.sendUiEvent(UiEvent.Navigate("search"))
                }, contentAlignment = Alignment.CenterEnd
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
        }
        Box(Modifier
            .fillMaxWidth()
            .weight(1f), contentAlignment = Alignment.Center) {
            Text(
                text = "Hello World!", fontSize = 20.sp,
                modifier = Modifier.clickable {
                    viewModel.loadData()
                }
            )
        }
    }
}