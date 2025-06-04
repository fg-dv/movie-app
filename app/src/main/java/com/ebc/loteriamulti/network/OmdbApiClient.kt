package com.ebc.loteriamulti.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OmdbApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: OmdbApiService = retrofit.create(OmdbApiService::class.java)
}