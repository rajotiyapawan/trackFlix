package com.rajotiyapawan.trackflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rajotiyapawan.trackflix.ui.FavouritesScreen
import com.rajotiyapawan.trackflix.ui.HomeScreen
import com.rajotiyapawan.trackflix.ui.MovieDetailView
import com.rajotiyapawan.trackflix.ui.SearchScreen
import com.rajotiyapawan.trackflix.ui.UiEvent
import com.rajotiyapawan.trackflix.ui.theme.TrackFlixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackFlixTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainViews(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun MainViews(modifier: Modifier = Modifier, viewModel: FlixViewModel = viewModel()) {
        LaunchedEffect(Unit) {
            viewModel.getConfigUrls()
            viewModel.initializeSearch()
        }
        val navController = rememberNavController()
        HandleUiEvent(navController)
        NavHost(modifier = modifier, navController = navController, startDestination = "home") {
            composable(route = "home") {
                HomeScreen(Modifier.fillMaxSize(), viewModel)
            }
            composable(route = "search") {
                SearchScreen(Modifier.fillMaxSize(), viewModel)
            }
            composable(route = "movieDetail") {
                MovieDetailView(Modifier.fillMaxSize(), viewModel)
            }
            composable(route = "favourites") {
                FavouritesScreen(Modifier.fillMaxSize(), viewModel)
            }
        }
    }

    @Composable
    fun HandleUiEvent(navController: NavHostController, viewModel: FlixViewModel = viewModel()) {
        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.Navigate -> {
                        if (event.route.isNotEmpty()) {
                            navController.navigate(event.route) {
                                launchSingleTop = true
                            }
                        }
                    }

                    UiEvent.DoNothing -> Unit
                }
            }
        }
    }
}