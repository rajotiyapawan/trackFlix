package com.rajotiyapawan.trackflix.network

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T): ApiResponse<T>()
    data class Error(val message: String, val code: Int? = null): ApiResponse<Nothing>()
}