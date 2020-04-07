package com.example.kitto.data
import android.os.Handler
import android.os.Looper
import com.example.kitto.BuildConfig
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MovieRepository {
    private val client = OkHttpClient()
    private var popularMovies: ArrayList<Movie> = ArrayList()
    private var popularPage = 1
    var totalPopularMovies = 0
    private var upcomingMovies: ArrayList<Movie> = ArrayList()
    private var upcomingPage = 1
    var totalUpcomingMovies = 0
    private var playingMovies: ArrayList<Movie> = ArrayList()
    private var playingPage = 1
    var totalPlayingMovies = 0
    private var topRatedMovies: ArrayList<Movie> = ArrayList()
    private var topRatedPage = 1
    var totalTopRatedMovies = 0
    private var searchedMovies: ArrayList<Movie> = ArrayList()
    private var searchPage = 1
    var totalSearchResults = 0
    private var searchCompleted = "False"
    lateinit var selectedMovie: Movie
    private val TMDB_API_KEY = BuildConfig.TMDB_API_KEY;
    private val mHandler = Handler(Looper.getMainLooper())
    private var query:String = ""

    companion object {
        private var sInstance: MovieRepository? = null
        fun instance(): MovieRepository {
            if (sInstance == null) {
                synchronized(MovieRepository) {
                    sInstance = MovieRepository()
                }
            }
            return sInstance!!
        }
    }

    private fun getMovieData(jsonMovie:JSONObject):Movie {
        //API Request
        val title = jsonMovie.getString("original_title")
        val id = jsonMovie.getString("id")
        var year =jsonMovie.getString("release_date")
        year = if(year.isNotEmpty()) year.substring(0,4)
        else "N/A"

        val posterPath = jsonMovie.getString("poster_path")
        val overview = jsonMovie.getString("overview")

        val movie = Movie(title, id, year, overview, posterPath)
        movie.vote_average = jsonMovie.getString("vote_average")
        movie.releaseDate = jsonMovie.getString("release_date")
        return movie
    }




    fun fetchPopularMovies(completion: (ArrayList<Movie>?, Error?)-> Unit){

        val url = "https://api.themoviedb.org/3/movie/popular?api_key=$TMDB_API_KEY&language=en-US&page=$popularPage"
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        call.enqueue( object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val jsonObject = JSONObject(responseData)
                val results = JSONArray(jsonObject.getString("results"))
                totalPopularMovies = jsonObject.getInt("total_results")
                for (i in 0 until results.length() ){
                    val jsonMovie = JSONObject(results[i].toString())
                    popularMovies.add(getMovieData(jsonMovie))
                }
                mHandler.post {
                    println("Popular Movies: $responseData")
                    popularPage++
                    completion(popularMovies, null)

                }


            }

            override fun onFailure(call: Call, e: IOException) {

                println("Request Failure")
                mHandler.post {
                    completion(null, Error(e.message))
                }
            }

        })

    }

    fun fetchNowPlayingMovies(completion: (ArrayList<Movie>?, Error?)-> Unit){

        val url = "https://api.themoviedb.org/3/movie/now_playing?api_key=$TMDB_API_KEY&language=en-US&page=$playingPage"
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        call.enqueue( object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val jsonObject = JSONObject(responseData)
                totalPlayingMovies = jsonObject.getInt("total_results")
                val results = JSONArray(jsonObject.getString("results"))
                for (i in 0 until results.length() ){
                    val jsonMovie = JSONObject(results[i].toString())
                    playingMovies.add(getMovieData(jsonMovie))
                }
                mHandler.post {
                    playingPage++
                    completion(playingMovies, null)

                }

            }

            override fun onFailure(call: Call, e: IOException) {

                println("Request Failure")
                mHandler.post {
                    completion(null, Error(e.message))
                }
            }

        })
    }

    fun fetchTopRatedMovies(completion: (ArrayList<Movie>?, Error?)-> Unit){

        val url = "https://api.themoviedb.org/3/movie/top_rated?api_key=$TMDB_API_KEY&language=en-US&page=$topRatedPage"
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        call.enqueue( object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val jsonObject = JSONObject(responseData)
                val results = JSONArray(jsonObject.getString("results"))
                totalTopRatedMovies = jsonObject.getInt("total_results")
                for (i in 0 until results.length() ){
                    val jsonMovie = JSONObject(results[i].toString())
                    topRatedMovies.add(getMovieData(jsonMovie))
                }
                mHandler.post {
                    topRatedPage++
                    completion(topRatedMovies, null)
                }


            }

            override fun onFailure(call: Call, e: IOException) {

                println("Request Failure")
                mHandler.post {
                    completion(null, Error(e.message))
                }
            }

        })

    }

    fun fetchUpcomingMovies(completion: (ArrayList<Movie>?, Error?)-> Unit){

        val url = "https://api.themoviedb.org/3/movie/upcoming?api_key=$TMDB_API_KEY&language=en-US&page=$upcomingPage"
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        call.enqueue( object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()

                val jsonObject = JSONObject(responseData)
                totalUpcomingMovies = jsonObject.getInt("total_results")
                val results = JSONArray(jsonObject.getString("results"))
                for (i in 0 until results.length() ){
                    val jsonMovie = JSONObject(results[i].toString())
                    upcomingMovies.add(getMovieData(jsonMovie))
                }
                mHandler.post {
                    upcomingPage++
                    completion(upcomingMovies, null)
                }

            }

            override fun onFailure(call: Call, e: IOException) {

                println("Request Failure")
                mHandler.post {
                    completion(null, Error(e.message))
                }
            }

        })

    }
    fun setQuery(string:String){
        searchedMovies.clear()
        searchCompleted = "False"
        searchPage = 1
        query = string.replace(" ", "%20")
    }

    fun fetchSearchResults(completion: (ArrayList<Movie>?, Error?)-> Unit){
        //Search results are cleared
        val url = "https://api.themoviedb.org/3/search/movie?api_key=$TMDB_API_KEY&language=en-US&query=$query&page=$searchPage&include_adult=false"
        val request = Request.Builder()
            .url(url)
            .build()
        val call = client.newCall(request)
        if(searchCompleted == "False"){
            call.enqueue( object: Callback {

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()

                    val jsonObject = JSONObject(responseData)
                    val totalPages = jsonObject.getInt("total_pages")
                    totalSearchResults = jsonObject.getInt("total_results")
                    val results = JSONArray(jsonObject.getString("results"))
                    for (i in 0 until results.length() ){
                        val jsonMovie = JSONObject(results[i].toString())
                        searchedMovies.add(getMovieData(jsonMovie))
                    }
                    mHandler.post {
                        if(searchPage == totalPages) searchCompleted = "True"
                        else searchPage++
                        completion(searchedMovies, null)

                    }

                }

                override fun onFailure(call: Call, e: IOException) {

                    println("Request Failure")
                    mHandler.post {
                        completion(null, Error(e.message))
                    }
                }

            })
        }else{
            completion(searchedMovies, null)
        }


    }




    fun fetchMovieIMDBId(movie: Movie?, completion: (Movie?, Error?) -> Unit) {

            val url = "https://api.themoviedb.org/3/movie/${movie?.id}?api_key=$TMDB_API_KEY&language=en-US&append_to_response=videos"
            val request = Request.Builder()
                .url(url)
                .build()
            val call = client.newCall(request)
            call.enqueue( object: Callback {

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()

                    val jsonObject = JSONObject(responseData)
                    val videos = JSONArray(JSONObject(jsonObject.getString("videos")).getString("results"))

                    if(videos.length() > 0 ) {
                        val video = JSONObject(videos[0].toString())
                        movie?.videoKey = video.getString("key")
                    }

                    movie?.imdbID = jsonObject.getString("imdb_id")
                    println("IMDB Id: " + movie?.imdbID)

                    mHandler.post {
                        completion(movie, null)

                    }

                }

                override fun onFailure(call: Call, e: IOException) {

                    println("Request Failure")
                    mHandler.post {
                        completion(null, Error(e.message))
                    }

                }
            })

    }

    fun fetchMovieInfo(movie: Movie?, completion: (Movie?, Error?) -> Unit){

        val url = "https://movie-database-imdb-alternative.p.rapidapi.com/?i=${movie?.imdbID}&r=json"
        val request = Request.Builder()
            .url(url)
            .header("X-RapidAPI-Host", "movie-database-imdb-alternative.p.rapidapi.com")
            .header("X-RapidAPI-Key", BuildConfig.RapidAPI_Key)
            .build()

        val call = client.newCall(request)
        call.enqueue( object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val jsonObject = JSONObject(responseData)
                if(jsonObject.getString("Response") == "True"){

                    movie?.genre = jsonObject.getString("Genre")
                    movie?.runtime = jsonObject.getString("Runtime")
                    movie?.rated = jsonObject.getString("Rated")
                    movie?.language = jsonObject.getString("Language")
                    movie?.actor = jsonObject.getString("Actors")
                    movie?.awards = jsonObject.getString("Awards")
                    movie?.country = jsonObject.getString("Country")
                    movie?.writer = jsonObject.getString("Writer")
                    movie?.releaseDate = jsonObject.getString("Released")
                    movie?.director = jsonObject.getString("Director")
                    movie?.ratingIMDB = jsonObject.getString("imdbRating")
                    movie?.ratingMetacritic = jsonObject.getString("Metascore")
                    val ratingsArray = JSONArray(jsonObject.getString("Ratings"))
                    for (i in 0 until ratingsArray.length() ){
                        val jsonRating = JSONObject(ratingsArray[i].toString())
                        if(jsonRating.get("Source") == "Rotten Tomatoes")
                            movie?.ratingRotten = jsonRating.getString("Value")
                    }

                }
                mHandler.post {
                    completion(movie, null)

                }

            }

            override fun onFailure(call: Call, e: IOException) {

                println("Request Failure")
                mHandler.post {
                    completion(null, Error(e.message))
                }

            }
        })
    }

    fun getQuery() = query

}



