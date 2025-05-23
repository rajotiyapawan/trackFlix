package com.rajotiyapawan.trackflix.ui

sealed class UiEvent {
    data class Navigate(val route:String): UiEvent()
}