package com.ebc.loteriamulti.network

import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {

    @GET(".")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = "7e78d4b8"
    ): MovieResponse

    @GET(".")
    suspend fun getMovieDetails(
        @Query("i") imdbID: String,
        @Query("apikey") apiKey: String = "7e78d4b8"
    ): Movie
}