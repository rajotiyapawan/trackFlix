package com.rajotiyapawan.trackflix.network

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface GenericAPIService {
    @GET
    suspend fun get(@Url url: String): String

    @POST
    suspend fun post(@Url url: String, @Body body: RequestBody): String
}