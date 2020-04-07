package com.example.kitto.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.kitto.R
import com.example.kitto.data.Movie



class MovieListAdapter: RecyclerView.Adapter<MovieListAdapter.BaseViewHolder>(){

    //Initialize variables
    private var movies =  ArrayList<Movie>()
    private val TYPE_MOVIE = 0
    private val TYPE_LOADER = 1
    private val TYPE_NONE = 2
    private var totalMovies = movies.size
    lateinit var mContext:Context
    private val TMDB_IMAGE_BASE_URL ="http://image.tmdb.org/t/p/"

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): BaseViewHolder {
        return when(viewType){
            TYPE_MOVIE -> {
                
                // Create a movie item view
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_list_item, parent, false) as View
                MovieViewHolder(itemView)
            }
            TYPE_LOADER -> {
                // Create a view containing a spinner
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.spinner_item, parent, false) as View
                LoadingViewHolder(itemView)
            }

            else ->{
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.spinner_item, parent, false) as View
                LoadingViewHolder(itemView)
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(movies.size){
            0 -> TYPE_NONE
            position -> TYPE_LOADER
            else -> TYPE_MOVIE
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }
    // Return the size of your data set (invoked by the layout manager)
    override fun getItemCount(): Int {
        return if(movies.size == totalMovies) movies.size
        else movies.size + 1
    }

    //Base class
    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(position: Int)
    }

    //Inner class MovieViewHolder
    inner class MovieViewHolder(view: View) : BaseViewHolder(view){
        var titleView: TextView = view.findViewById(R.id.movie_item_title)
        var dateView: TextView = view.findViewById(R.id.movie_item_release_date)
        var overviewView: TextView = view.findViewById(R.id.movie_item_overview)
        var posterView: ImageView = view.findViewById(R.id.movie_item_image)
        override fun bind(position:Int) {
            val movie = movies[position]
            val posterSize = "w92"
            val posterPath = TMDB_IMAGE_BASE_URL + posterSize + movie.poster
            titleView.text = movie.title
            dateView.text = movie.year
            overviewView.text = movie.overview
            Glide.with(mContext)
                .load(posterPath)
                .placeholder(R.drawable.ic_local_movies_light_blue_24dp)
                .into(posterView)
        }



    }

    inner class LoadingViewHolder(view: View): BaseViewHolder(view){
        private val spinner: ProgressBar = view.findViewById(R.id.item_spinner)

        override fun bind(position: Int) {
            if (position == 0) spinner.visibility = View.GONE
            else spinner.visibility = View.VISIBLE
        }

    }

    fun setMovieList(movieList: ArrayList<Movie>) {
        movies = movieList
    }

    fun setTotalResults(number:Int){
        totalMovies = number
    }


}