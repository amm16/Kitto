package com.example.kitto.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.kitto.BuildConfig

import com.example.kitto.R
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.*





class MovieDetailsFragment : Fragment() {

    private val YOUTUBE_PLAYER_FRAGMENT = "YOUTUBE_PLAYER_FRAGMENT"
    private var YOUTUBE_API_KEY = BuildConfig.YOUTUBE_API_KEY;
    private val TMDB_IMAGE_BASE_URL ="http://image.tmdb.org/t/p/"
    private val TAG = MovieDetailsFragment::class.java.simpleName

    private val youtubeFragment = YouTubePlayerSupportFragment()
    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var viewModel: DetailsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("On create is called")
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val titleView = view.findViewById<TextView>(R.id.movie_title)
        val posterView = view.findViewById<ImageView>(R.id.movie_poster)
        val ratedView =view.findViewById<TextView>(R.id.movie_rated)
        val genreView = view.findViewById<TextView>(R.id.movie_genre)
        val yearView = view.findViewById<TextView>(R.id.movie_year)
        val imdbView = view.findViewById<TextView>(R.id.imdb_rating)
        val rottenView = view.findViewById<TextView>(R.id.rotten_tomatoes_rating)
        val metacriticView = view.findViewById<TextView>(R.id.metacritic_rating)
        val plotView = view.findViewById<TextView>(R.id.movie_overview)
        val writersView = view.findViewById<TextView>(R.id.movie_writers)
        val actorsView = view.findViewById<TextView>(R.id.movie_actors)
        val runtimeView = view.findViewById<TextView>(R.id.movie_runtime)
        val dateView = view.findViewById<TextView>(R.id.movie_release_date)
        val directorView = view.findViewById<TextView>(R.id.movie_director)
        val languageView = view.findViewById<TextView>(R.id.movie_language)
        val countryView = view.findViewById<TextView>(R.id.movie_country)
        val awardsView = view.findViewById<TextView>(R.id.movie_awards)
        val movie = viewModel.getSelectedMovie().value
        val posterSize = "w300"
        val posterPath = TMDB_IMAGE_BASE_URL + posterSize + movie?.poster

        Glide.with(getApplicationContext())
            .load(posterPath)
            .placeholder(R.drawable.ic_local_movies_dark_blue_24dp)
            .into(posterView)
        titleView.text = movie?.title
        plotView.text = movie?.overview
        ratedView.text= movie?.rated
        imdbView.text = movie?.ratingIMDB
        rottenView.text = movie?.ratingRotten
        metacriticView.text = movie?.ratingMetacritic
        genreView.text = movie?.genre
        yearView.text = movie?.year
        actorsView.text = movie?.actor
        writersView.text = movie?.writer
        runtimeView.text = movie?.runtime
        dateView.text = movie?.releaseDate
        directorView.text = movie?.director
        languageView.text = movie?.language
        countryView.text = movie?.country
        awardsView.text = movie?.country


        if(viewModel.getSelectedMovie().value?.videoKey != null){
            val  transaction = childFragmentManager.beginTransaction()
            transaction.add(R.id.youtube_player_fragment,youtubeFragment,YOUTUBE_PLAYER_FRAGMENT).commit()
            initializeYoutubePlayer()
        }


        return view
    }

    private fun initializeYoutubePlayer() {
        println("Youtube Player is initialized")
        println("Youtube Player Fragment isn't null")
        youtubeFragment.initialize(YOUTUBE_API_KEY, object : OnInitializedListener {

            override fun onInitializationSuccess(
                provider: Provider, player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    println("Youtube Player initialization was not restored")

                    val playbackEventListener = object : PlaybackEventListener {
                        override fun onStopped() {
                        }

                        override fun onBuffering(arg0: Boolean) {}

                        override fun onPaused() {}

                        override fun onPlaying() {
                            youTubePlayer?.addFullscreenControlFlag(FULLSCREEN_FLAG_CONTROL_ORIENTATION)
                            youTubePlayer?.addFullscreenControlFlag(FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE)

                        }

                        override fun onSeekTo(arg0: Int) {}
                    }

                    youTubePlayer = player
                    youTubePlayer?.setPlaybackEventListener(playbackEventListener)

                    //set the player style default
                    youTubePlayer?.setPlayerStyle(PlayerStyle.DEFAULT)

                    //cue the video by default
                    youTubePlayer?.loadVideo(viewModel.getSelectedMovie().value?.videoKey)

                    youTubePlayer?.play()

                    if(youTubePlayer?.isPlaying == true){

                    }
                }
                println("Youtube Player initialization was successful")
            }

            override fun onInitializationFailure(arg0: Provider, arg1: YouTubeInitializationResult) {

                //print or show error if initialization failed
                println("Youtube Player initialization failed")

                Log.e(TAG, "Youtube Player View initialization failed")
            }
        })
    }
}
