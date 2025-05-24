package com.rajotiyapawan.trackflix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajotiyapawan.trackflix.network.ApiResponse
import com.rajotiyapawan.trackflix.network.NetworkRepository
import com.rajotiyapawan.trackflix.network.TMDB_BaseUrl
import com.rajotiyapawan.trackflix.ui.UiEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class FlixViewModel : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent get() = _uiEvent

    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    fun clearUiEvent() {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.DoNothing)
        }
    }

    private var _posterPath = ""
    val posterPath get() = _posterPath

    private var _configData: ConfigData? = null
    val configData get() = _configData
    fun getConfigUrls() {
        viewModelScope.launch {
            when (val response = NetworkRepository.get<ConfigData>("${TMDB_BaseUrl}configuration")) {
                is ApiResponse.Error -> {}
                ApiResponse.Idle -> {}
                is ApiResponse.Success<ConfigData> -> {
                    _configData = response.data
                    _posterPath = response.data.images.secure_base_url + response.data.images.poster_sizes[1]
                }
            }
        }
    }

    private var _trendingMovies = MutableStateFlow<DiscoverMovieList?>(null)
    val trendingMovies = _trendingMovies.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            when (val response = NetworkRepository.get<DiscoverMovieList>("${TMDB_BaseUrl}trending/movie/day?language=en-US")) {
                is ApiResponse.Error -> {}
                ApiResponse.Idle -> {}
                is ApiResponse.Success<DiscoverMovieList> -> {
                    _trendingMovies.value = response.data
                }
            }
        }
    }

    private var _nowPlayingMovies = MutableStateFlow<DiscoverMovieList?>(null)
    val nowPlayingMovies = _nowPlayingMovies.asStateFlow()

    fun loadNowPlayingData() {
        viewModelScope.launch {
            when (val response = NetworkRepository.get<DiscoverMovieList>("${TMDB_BaseUrl}movie/now_playing?language=en-US&page=1")) {
                is ApiResponse.Error -> {}
                ApiResponse.Idle -> {}
                is ApiResponse.Success<DiscoverMovieList> -> {
                    _nowPlayingMovies.value = response.data
                }
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

    private var _selectedMovie = MutableStateFlow<MovieData?>(null)
    val selectedMovie = _selectedMovie.asStateFlow()

    fun getMovieDetails(id: Int?) {
        viewModelScope.launch {
            when (val response = NetworkRepository.get<MovieData>("${TMDB_BaseUrl}movie/${id}?language=en-US")) {
                is ApiResponse.Error -> {}
                ApiResponse.Idle -> {}
                is ApiResponse.Success<MovieData> -> {
                    _selectedMovie.value = response.data
                }
            }
        }
    }

    fun clearMovieDetails() {
        _selectedMovie.value = null
    }

    private var _favouritesList = MutableStateFlow<List<MovieData>>(emptyList())
    val favouritesList = _favouritesList.asStateFlow()
    fun getFavourites() {
        viewModelScope.launch {
            _favouritesList.value = trendingMovies.value?.results ?: emptyList()
        }
    }

}