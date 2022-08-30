package com.elephant.spaceimage.data.nasa_api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {
    @GET("planetary/apod")
    suspend fun getPicture(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Response<NasaServerResponse>
}