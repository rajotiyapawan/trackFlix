package com.rajotiyapawan.trackflix.presentation.ui

sealed class UiEvent {
    data class Navigate(val route:String): UiEvent()
    data object DoNothing : UiEvent()
    data object BackBtnClicked : UiEvent()
}