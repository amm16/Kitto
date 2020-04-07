package com.example.kitto.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.kitto.R
import com.example.kitto.data.Movie
import com.example.kitto.data.MovieRepository

class ListViewModel: ViewModel() {

    private var selectedMovieList = MutableLiveData<ArrayList<Movie>>()
    private var selectedMovie = MutableLiveData<Movie?>()
    private var popularMovies= MutableLiveData<ArrayList<Movie>>()
    private var upcomingMovies= MutableLiveData<ArrayList<Movie>>()
    private var playingMovies= MutableLiveData<ArrayList<Movie>>()
    private var topRatedMovies= MutableLiveData<ArrayList<Movie>>()
    private var searchedMovies= MutableLiveData<ArrayList<Movie>>()
    private var listNameID = MutableLiveData<Int>()
    private var currentPage = MutableLiveData<Int>()

    private val movieRepository = MovieRepository.instance()

    init {
        loadNowPlayingMovies()
        loadPopularMovies()
        loadUpcomingMovies()
        loadTopRatedMovies()
        currentPage.value = 1
    }

    fun loadPopularMovies() {
        movieRepository.fetchPopularMovies { movies, error ->
            if(error == null){
                popularMovies.value = movies
                if (selectedMovieList.value == null){
                    selectedMovieList.value = popularMovies.value
                    listNameID.value = R.string.popular_movies_label
                }
            }
        }
    }

    fun loadUpcomingMovies() {
        movieRepository.fetchUpcomingMovies { movies, error ->
            if(error == null){
                upcomingMovies.value = movies
            }
        }
    }

    fun loadNowPlayingMovies() {
        movieRepository.fetchNowPlayingMovies { movies, error ->
            if(error == null){
                playingMovies.value = movies
            }
        }
    }

    fun loadTopRatedMovies() {
        movieRepository.fetchTopRatedMovies { movies, error ->
            if(error == null){
                topRatedMovies.value = movies
            }
        }
    }

    fun searchMovie(query:String){
        movieRepository.setQuery(query)
        movieRepository.fetchSearchResults{ movies, error ->
            if(error == null){
                searchedMovies.value= movies
            }
        }
    }
    fun loadSearchedMovies(){
        movieRepository.fetchSearchResults{ movies, error ->
            if(error == null){
                searchedMovies.value= movies
            }
        }
    }


    fun selectMovie(movie:Movie?){
        if (movie != null){
            movieRepository.selectedMovie = movie
        }
        selectedMovie.value = movie
    }

    fun selectPage(number:Int){
        currentPage.value = number

    }

    fun getSelectedMovieList() = selectedMovieList
    fun getPopularMovies() = popularMovies
    fun getUpcomingMovies() = upcomingMovies
    fun getPlayingNowMovies() = playingMovies
    fun getTopRatedMovies() = topRatedMovies
    fun getSearchedMovies() = searchedMovies
    fun getSelectedMovie() = selectedMovie
    fun getQuery() = movieRepository.getQuery()
    fun getListName() = listNameID
    fun getTotalSearchResults() = movieRepository.totalSearchResults
    fun getTotalPopularMovies() = movieRepository.totalPopularMovies
    fun getTotalUpcomingMovies() = movieRepository.totalUpcomingMovies
    fun getTotalPlayingNowMovies() = movieRepository.totalPlayingMovies
    fun getTotalTopRatedMovies() = movieRepository.totalTopRatedMovies
    fun getCurrentPage() = currentPage

    fun clearQuery(){
        movieRepository.setQuery("")
    }


}
