package com.rajotiyapawan.trackflix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajotiyapawan.trackflix.domain.model.ConfigData
import com.rajotiyapawan.trackflix.domain.model.DiscoverMovieList
import com.rajotiyapawan.trackflix.domain.model.MovieCategory
import com.rajotiyapawan.trackflix.domain.model.MovieData
import com.rajotiyapawan.trackflix.domain.usecase.AddToFavouriteUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetConfigDataUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetFavoriteMoviesUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetIsMovieBookmarkedUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetMovieDetailsUseCase
import com.rajotiyapawan.trackflix.domain.usecase.GetMoviesUseCase
import com.rajotiyapawan.trackflix.domain.usecase.RemoveFromFavoritesUseCase
import com.rajotiyapawan.trackflix.network.ApiResponse
import com.rajotiyapawan.trackflix.network.NetworkRepository
import com.rajotiyapawan.trackflix.network.TMDB_BaseUrl
import com.rajotiyapawan.trackflix.presentation.ui.UiEvent
import com.rajotiyapawan.trackflix.utils.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class FlixViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getConfigDataUseCase: GetConfigDataUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val addToFavouriteUseCase: AddToFavouriteUseCase,
    private val getIsMovieBookmarkedUseCase: GetIsMovieBookmarkedUseCase
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent get() = _uiEvent

    fun sendUiEvent(event: UiEvent, withDelay: Boolean = false) {
        viewModelScope.launch {
            if (withDelay) {
                delay(1000)
            }
            _uiEvent.emit(event)
        }
    }

    fun clearUiEvent() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.DoNothing)
        }
    }

    private var _configData: ConfigData? = null
    val configData get() = _configData
    fun getConfigUrls() {
        viewModelScope.launch {
            getConfigDataUseCase().collectLatest {
                when (it) {
                    is UiState.Success<ConfigData> -> _configData = it.data
                    else -> {}
                }
            }
        }
    }

    private var _trendingMovies = MutableStateFlow<UiState<DiscoverMovieList>>(UiState.Idle)
    val trendingMovies = _trendingMovies.asStateFlow()

    fun getTrendingMovies() {
        viewModelScope.launch {
            getMoviesUseCase.invoke(MovieCategory.TRENDING).collectLatest {
                _trendingMovies.value = it
            }
        }
    }

    private var _nowPlayingMovies = MutableStateFlow<UiState<DiscoverMovieList>>(UiState.Idle)
    val nowPlayingMovies = _nowPlayingMovies.asStateFlow()

    fun loadNowPlayingData() {
        viewModelScope.launch {
            getMoviesUseCase.invoke(MovieCategory.NOW_PLAYING).collectLatest {
                _nowPlayingMovies.value = it
            }
        }
    }

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MovieData>>(emptyList())
    val searchResults: StateFlow<List<MovieData>> = _searchResults.asStateFlow()

    private var searchJob: Job? = null

    @OptIn(FlowPreview::class)
    fun initializeSearch() {
        searchJob = viewModelScope.launch {
            _query.debounce(500) // wait for user to stop typing
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.length >= 3) {
                        _searchResults.value = emptyList()
                        when (val response = NetworkRepository.get<DiscoverMovieList>("${TMDB_BaseUrl}search/movie?query=${query}&include_adult=false&language=en-US&page=1")) {
                            is ApiResponse.Success<DiscoverMovieList> -> {
                                response.data.results?.let { results ->
                                    _searchResults.value = results.filter { it.poster_path?.isNotEmpty() == true }
                                }
                            }

                            else -> {}
                        }
                    } else {
                        _searchResults.value = emptyList()
                    }
                }
        }
    }

    // stops the searching job after exiting the search screen
    fun stopSearching() {
        searchJob?.cancel()
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    private var _selectedMovie = MutableStateFlow<UiState<MovieData>>(UiState.Idle)
    val selectedMovie = _selectedMovie.asStateFlow()

    fun getMovieDetails(movie: MovieData) {
        if (movie.id != null) {
            loadFavoriteStatus(movie.id)
        }
        viewModelScope.launch {
            getMovieDetailsUseCase.invoke(movie = movie).collectLatest {
                _selectedMovie.value = it
            }
        }
    }

    private val _isBookmarked = MutableStateFlow(false)
    val isBookmarked: StateFlow<Boolean> = _isBookmarked

    private fun loadFavoriteStatus(movieId: Int) {
        viewModelScope.launch {
            getIsMovieBookmarkedUseCase(movieId).collect {
                _isBookmarked.value = it
            }
        }
    }

    fun clearMovieDetails() {
        _selectedMovie.value = UiState.Idle
    }

    private var _favouritesList = MutableStateFlow<List<MovieData>>(emptyList())
    val favouritesList = _favouritesList.asStateFlow()
    fun getFavourites() {
        viewModelScope.launch {
            getFavoriteMoviesUseCase().collectLatest {
                _favouritesList.value = it
            }
        }
    }

    fun toggleFavourite(movie: MovieData) {
        viewModelScope.launch {
            if (isBookmarked.value) {
                removeFromFavoritesUseCase(movie)
            } else {
                addToFavouriteUseCase(movie)
            }
            _isBookmarked.value = !isBookmarked.value
        }
    }
}