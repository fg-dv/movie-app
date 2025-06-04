package com.ebc.loteriamulti.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebc.loteriamulti.network.Movie
import com.ebc.loteriamulti.network.OmdbApiClient
import kotlinx.coroutines.launch

class PeliculasViewModel : ViewModel() {

    var movies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var selectedMovie: Movie? by mutableStateOf(null)
        private set

    fun search(query: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = OmdbApiClient.service.searchMovies(query = query)
                if (response.Response == "True") {
                    val details = response.Search.mapNotNull {
                        try {
                            OmdbApiClient.service.getMovieDetails(it.imdbID)
                        } catch (_: Exception) {
                            null
                        }
                    }
                    movies = details
                } else {
                    movies = emptyList()
                }
            } catch (e: Exception) {
                movies = emptyList()
            }
            isLoading = false
        }
    }

    fun selectMovie(movie: Movie) {
        selectedMovie = movie
    }

    fun clearSelectedMovie() {
        selectedMovie = null
    }
}
