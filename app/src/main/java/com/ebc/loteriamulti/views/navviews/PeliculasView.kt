package com.ebc.loteriamulti.views.navviews

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ebc.loteriamulti.viewmodel.PeliculasViewModel
import com.ebc.loteriamulti.network.Movie

@Composable
fun PeliculasView(viewModel: PeliculasViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }

    val movies = viewModel.movies
    val isLoading = viewModel.isLoading
    val selectedMovie = viewModel.selectedMovie

    Column(modifier = Modifier.padding(16.dp)) {
        if (selectedMovie != null) {
            MovieDetailCard(movie = selectedMovie) {
                viewModel.clearSelectedMovie()
            }
        } else {
            TextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar película") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.search(query) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Buscar")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn {
                    items(movies) { movie ->
                        MovieListItem(movie = movie) {
                            viewModel.selectMovie(movie)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieListItem(movie: Movie, onClick: () -> Unit) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = movie.Poster,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = movie.Title, style = MaterialTheme.typography.h6)
                Text(text = "Año: ${movie.Year}", style = MaterialTheme.typography.body2)
                Text(text = "Director: ${movie.Director}", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
fun MovieDetailCard(movie: Movie, onBack: () -> Unit) {
    Card(
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = movie.Title, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                model = movie.Poster,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Año: ${movie.Year}")
            Text("Clasificación: ${movie.Rated}")
            Text("Estreno: ${movie.Released}")
            Text("Duración: ${movie.Runtime}")
            Text("Género: ${movie.Genre}")
            Text("Director: ${movie.Director}")
            Text("Guionistas: ${movie.Writer}")
            Text("Actores: ${movie.Actors}")
            Text("Idioma: ${movie.Language}")
            Text("País: ${movie.Country}")
            Text("Premios: ${movie.Awards}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Sinopsis:", style = MaterialTheme.typography.subtitle1)
            Text(movie.Plot)

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onBack() }, modifier = Modifier.align(Alignment.End)) {
                Text("Volver al buscador")
            }
        }
    }
}