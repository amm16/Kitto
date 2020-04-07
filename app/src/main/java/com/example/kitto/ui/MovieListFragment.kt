package com.example.kitto.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.DividerItemDecoration
import android.widget.ProgressBar
import android.widget.Toast
import com.facebook.FacebookSdk.getApplicationContext
import com.example.kitto.R
import com.example.kitto.data.Movie



class MovieListFragment() : Fragment(){
    private lateinit var mMoviesList: RecyclerView
    private var mAdapter: MovieListAdapter = MovieListAdapter()
    private lateinit var viewModel: ListViewModel



    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

    }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            //activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            // Inflate the layout for this fragment
            val rootView: View = inflater.inflate(R.layout.movie_list_fragment, container, false)
            //Get a reference to RecycleView
            mMoviesList = rootView.findViewById(R.id.rv_movie_list)
            //Set LayoutManager
            mMoviesList.layoutManager = LinearLayoutManager(getApplicationContext())
            mAdapter.mContext = getApplicationContext()
            //click Listener
            mMoviesList.addOnItemTouchListener(
                RecyclerTouchListener(
                    getApplicationContext(),
                    mMoviesList,
                    object : RecyclerTouchListener.ClickListener {
                        override fun onClick(view: View, position: Int) {
                            val movie = viewModel.getSelectedMovieList().value!![position]
                            Toast.makeText(getApplicationContext(), movie.title + " is selected!", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.selectMovie(movie)

                        }

                        override fun onLongClick(view: View, position: Int) {

                        }
                    })
            )
            //Add RecyclerView Divider
            mMoviesList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

            //Set adapter
            mMoviesList.adapter = mAdapter
            //Set ItemAnimator
            mMoviesList.itemAnimator = DefaultItemAnimator()

            return rootView
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSelectedMovieList().observe(this, Observer<ArrayList<Movie>> {
            mAdapter.setMovieList(viewModel.getSelectedMovieList().value!!)
            println("on Fragment: " + viewModel.getSelectedMovieList().value!!.size)
            val spinner = view?.findViewById<ProgressBar>(R.id.list_spinner)
            if (viewModel.getSelectedMovieList().value?.size == null) {
                spinner?.visibility = View.VISIBLE
                Toast.makeText(
                    getApplicationContext(),
                    "You're offline. Please connect to the internet.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }else{
                spinner?.visibility = View.GONE
                mAdapter.notifyDataSetChanged()

            }
        })
    }


}
