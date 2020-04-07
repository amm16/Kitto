package com.example.kitto.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.kitto.data.Movie
import com.example.kitto.data.MovieRepository

class DetailsViewModel: ViewModel() {

    private var selectedMovie = MutableLiveData<Movie>()
    private val movieRepository = MovieRepository.instance()
    private var movie = movieRepository.selectedMovie


    init {
        loadSelectedMovie()
    }



    private fun loadSelectedMovie(){
        movieRepository.fetchMovieIMDBId(movie) { movie, error ->
            if(error == null){
                movieRepository.fetchMovieInfo(movie) { movie, error ->
                    if(error == null){
                            selectedMovie.value = movie
                        println("Viewmodel Selected Movie: " + movie?.title)

                    }else{
                        println("Error")
                    }
                }

            }else{
                println("Error")
            }
        }

    }

    fun getSelectedMovie(): MutableLiveData<Movie> {
        return selectedMovie
    }
    fun deselectMovie(){
        selectedMovie.value = null
    }

}
