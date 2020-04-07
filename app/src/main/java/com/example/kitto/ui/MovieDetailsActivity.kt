package com.example.kitto.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.example.kitto.R
import com.example.kitto.data.Movie
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager

import com.example.kitto.ui.ListViewModel as MovieViewModel




class MovieDetailsActivity : AppCompatActivity(){

    private var detailsFragment = MovieDetailsFragment()
    private val MOVIE_DETAILS_FRAGMENT = "MOVIE_DETAILS_FRAGMENT"

    private lateinit var viewModel:DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        if(savedInstanceState == null){
            detailsFragment = MovieDetailsFragment()
        }
        //Sets view model
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        println("On Activity: " + viewModel.getSelectedMovie().value?.title)
        val spinner = findViewById<ProgressBar>(R.id.details_spinner)
        //Replace Fragment
        viewModel.getSelectedMovie().observe(this, Observer<Movie?>{
            println("Observer called")
            if(viewModel.getSelectedMovie().value != null){
                println("Movie Value is not null: " + viewModel.getSelectedMovie().value?.title)
                spinner.visibility = View.GONE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.details_container,detailsFragment,MOVIE_DETAILS_FRAGMENT)
                    .commit()
            }else{
                println("Movie value is null")
            }


        })

    }

    override fun onBackPressed() {
        //viewModel.deselectMovie()
        println("Selected movie is now null")
        super.onBackPressed()
    }
}








