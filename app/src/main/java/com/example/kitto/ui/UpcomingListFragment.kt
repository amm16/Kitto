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
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.facebook.FacebookSdk.getApplicationContext
import com.example.kitto.R
import com.example.kitto.data.Movie



class UpcomingListFragment : Fragment(){
    private lateinit var mMoviesList: RecyclerView
    private var mAdapter = UpcomingListAdapter()
    private lateinit var viewModel: ListViewModel
    private val UPCOMING_LIST_TAG = "UPCOMING_LIST_TAG"

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
        mMoviesList.layoutManager = LinearLayoutManager(activity?.applicationContext)
        mAdapter.mContext = getApplicationContext()
        //click Listener
        mMoviesList.addOnItemTouchListener(
            RecyclerTouchListener(
                getApplicationContext(),
                mMoviesList,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View, position: Int) {
                        val movie = viewModel.getUpcomingMovies().value!![position]
                        Toast.makeText(getApplicationContext(), movie.title + " is selected!", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.selectMovie(movie)

                    }

                    override fun onLongClick(view: View, position: Int) {

                    }
                })
        )

        //scroll Listener
        mMoviesList.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!mMoviesList.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ){
                    Log.d(UPCOMING_LIST_TAG,"Reached Bottom")
                    mMoviesList.removeOnScrollListener(this)
                    viewModel.loadUpcomingMovies()
                    mMoviesList.addOnScrollListener(this)
                }

            }
        })
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
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        viewModel.getUpcomingMovies().observe(this, Observer<ArrayList<Movie>>{
            mAdapter.setMovieList(viewModel.getUpcomingMovies().value!!)
            mAdapter.setTotalResults(viewModel.getTotalUpcomingMovies())
            val spinner = view.findViewById<ProgressBar>(R.id.list_spinner)
            if (viewModel.getUpcomingMovies().value  == null ){
                spinner.visibility = View.VISIBLE
            }else{
                spinner.visibility = View.GONE
                mAdapter.notifyDataSetChanged()
            }

        })
    }



}
