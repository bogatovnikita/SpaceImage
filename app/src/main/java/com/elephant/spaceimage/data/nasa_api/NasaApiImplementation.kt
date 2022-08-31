package com.elephant.spaceimage.data.nasa_api

import com.elephant.spaceimage.App
import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory

class NasaApiImplementation {
    private val baseUrl = "https://api.nasa.gov/"
    fun getRetrofitImplementation(): NasaApi {

        val retrofit = App.retrofit
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return retrofit.create(NasaApi::class.java)
    }

}