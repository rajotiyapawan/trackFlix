package com.rajotiyapawan.trackflix.network

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

object NetworkRepository {
    private val gson = Gson()
    private val service: GenericAPIService = NetworkModule.retrofit.create(GenericAPIService::class.java)

    suspend fun <T : Any> get(
        url: String,
        responseClass: Class<T>
    ): ApiResponse<T> {
        return try {
            val response = service.get(url)
            val parsed = gson.fromJson(response, responseClass)
            ApiResponse.Success(parsed)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    suspend fun <T : Any> post(
        url: String,
        jsonBody: String,
        responseClass: Class<T>
    ): ApiResponse<T> {
        return try {
            val mediaType = "application/json".toMediaType()
            val body = jsonBody.toRequestBody(mediaType)
            val response = service.post(url, body)
            val parsed = gson.fromJson(response, responseClass)
            ApiResponse.Success(parsed)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private fun <T> handleError(e: Exception): ApiResponse<T> {
        return when (e) {
            is HttpException -> ApiResponse.Error("HTTP ${e.code()}: ${e.message()}", e.code())
            is IOException -> ApiResponse.Error("Network Error: ${e.message}")
            else -> ApiResponse.Error("Unknown Error: ${e.message}")
        }
    }
}