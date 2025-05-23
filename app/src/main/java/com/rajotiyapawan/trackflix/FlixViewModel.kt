package com.rajotiyapawan.trackflix

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rajotiyapawan.trackflix.network.NetworkRepository
import com.rajotiyapawan.trackflix.network.TMDB_BaseUrl
import com.rajotiyapawan.trackflix.ui.UiEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class FlixViewModel: ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent get() = _uiEvent

    fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }


    fun loadData() {
        viewModelScope.launch {
            val response = NetworkRepository.get("${TMDB_BaseUrl}movie/popular?language=en-US&page=1", DiscoverMovieList::class.java)
        }
    }

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults.asStateFlow()

    private var searchJob: Job? = null
    fun initializeSearch(){
        searchJob = viewModelScope.launch {
            _query
                .debounce(500) // wait for user to stop typing
//                .filter { it.length >= 3 } // only proceed if 3+ characters
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.length >= 3) {
                        _searchResults.value = performSearch(query)
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

    private suspend fun performSearch(query: String): List<String> {
        // Replace with your API call
        delay(500) // simulate network delay
        return listOf("Result 1 for $query", "Result 2 for $query", "Result 3 for $query")
    }

}