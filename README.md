
# Documentación de la App de Películas con Retrofit

Esta aplicación consume la API de OMDB para obtener información de películas. Se usó Retrofit para manejar las solicitudes HTTP, se definieron modelos para mapear la respuesta JSON, y se implementaron funciones para mostrar los datos en la interfaz.

---

## 1. Configuración de Retrofit

Para conectar la app con la API, se configuró Retrofit de la siguiente manera:

```kotlin
private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: OmdbApiService = retrofit.create(OmdbApiService::class.java)
```

- `baseUrl` es la URL base de la API.
- `GsonConverterFactory` para convertir la respuesta JSON en objetos Kotlin.

---

## 2. Definición del API Service

Se definió una interfaz que describe los endpoints que se usarán:

```kotlin
interface OmdbApiService {

    @GET(".")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = "7e78d4b8"
    ): MovieResponse
```

- `@GET("/")`: método GET a la ruta base.
- Parámetros con `@Query` para enviar la API key y el título de la película.
- La función retorna un `Response` que contiene un objeto `PeliculaResponse`.

---

## 3. Modelo de Datos

Se creó un data class para mapear los campos importantes de la respuesta JSON:

```kotlin
data class Movie(
    val Title: String,
    val Year: String,
    val Director: String,
    val Genre: String,
    val Plot: String,
    val Actors: String,
    val Language: String,
    val Country: String,
    val Awards: String
)
```

- Los nombres deben coincidir con los nombres del JSON para que Gson pueda mapear automáticamente.

---

## 4. Consumo de la API y Manejo de Datos

En el PeliculasViewModel se realiza la búsqueda de películas y luego se obtienen sus detalles uno por uno usando viewModelScope.launch:

```kotlin
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
```

- Se usa viewModelScope.launch para ejecutar la solicitud de forma asíncrona.

- Primero se busca la lista general de películas (searchMovies).

- Luego, por cada resultado, se consulta su detalle (getMovieDetails) usando su imdbID.

- Se actualiza el estado movies con la lista detallada.

- Se maneja un estado isLoading para mostrar indicadores de carga.

---

## 5. Mostrar datos en la interfaz

Se mostraron los datos usando Jetpack Compose:

```kotlin
Text("Año: ${movie.Year}")
Text("Clasificación: ${movie.Rated}")
Text("Estreno: ${movie.Released}")
Text("Duración: ${movie.Runtime}")
Text("Género: ${movie.Genre}")
Text("Director: ${movie.Director}")
```

---