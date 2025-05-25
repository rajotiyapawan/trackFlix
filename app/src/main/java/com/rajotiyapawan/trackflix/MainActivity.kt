package com.rajotiyapawan.trackflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rajotiyapawan.trackflix.data.local.MovieDatabase
import com.rajotiyapawan.trackflix.data.repository.LocalMovieRepositoryImpl
import com.rajotiyapawan.trackflix.data.repository.RemoteMovieRepositoryImpl
import com.rajotiyapawan.trackflix.domain.model.getMovieData
import com.rajotiyapawan.trackflix.domain.usecase.AddToFavouriteUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetConfigDataUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetFavoriteMoviesUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetIsMovieBookmarkedUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetMovieDetailsUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetMoviesUseCase
import com.rajotiyapawan.trackflix.domain.usecase.RemoveFromFavoritesUseCase
import com.rajotiyapawan.trackflix.presentation.ui.FavouritesScreen
import com.rajotiyapawan.trackflix.presentation.ui.HomeScreen
import com.rajotiyapawan.trackflix.presentation.ui.MovieDetailView
import com.rajotiyapawan.trackflix.presentation.ui.SearchScreen
import com.rajotiyapawan.trackflix.presentation.ui.UiEvent
import com.rajotiyapawan.trackflix.utils.isNetworkAvailable

class MainActivity : ComponentActivity() {

    private val viewModel: FlixViewModel by viewModels {
        viewModelFactory {
            val isNetworkAvailable = isNetworkAvailable(this@MainActivity)
            initializer {
                val dao = MovieDatabase.getInstance(applicationContext).movieDao()
                val repo = RemoteMovieRepositoryImpl()
                val localRepo = LocalMovieRepositoryImpl(dao)
                FlixViewModel(
                    GetMoviesUseCase(repo, localRepo, isNetworkAvailable),
                    GetMovieDetailsUseCase(repo, isNetworkAvailable),
                    GetConfigDataUseCase(repo),
                    GetFavoriteMoviesUseCase(localRepo),
                    RemoveFromFavoritesUseCase(localRepo),
                    AddToFavouriteUseCase(localRepo),
                    GetIsMovieBookmarkedUseCase(localRepo)
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieIdFromDeepLink = intent?.data?.getQueryParameter("id")?.toIntOrNull()
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                MainViews(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding), viewModel = viewModel, movieIdFromDeepLink
                )
            }
        }
    }

    @Composable
    private fun MainViews(modifier: Modifier = Modifier, viewModel: FlixViewModel, movieIdFromDeepLink: Int?) {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            if (isNetworkAvailable(context)) {
                viewModel.initializeSearch()
            }
        }
        val navController = rememberNavController()
        LaunchedEffect(movieIdFromDeepLink) {
            movieIdFromDeepLink?.let { id ->
                navController.navigate("movieDetail")
                viewModel.getMovieDetails(getMovieData().copy(id = id))
            }
        }
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
                    UiEvent.BackBtnClicked -> {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}