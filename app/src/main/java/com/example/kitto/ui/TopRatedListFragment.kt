package com.example.kitto.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.facebook.FacebookSdk.getApplicationContext
import com.example.kitto.R
import com.example.kitto.data.Movie


class TopRatedListFragment : Fragment(){
    private lateinit var mMoviesRecycler: RecyclerView
    private var mAdapter = TopRatedListAdapter()
    private lateinit var viewModel: ListViewModel
    private val TOP_RATED_LIST_TAG = "TOP_RATED"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.movie_list_fragment, container, false)
        //Get a reference to RecycleView
        mMoviesRecycler = rootView.findViewById(R.id.rv_movie_list)
        val orientation = activity?.resources?.configuration?.orientation
        //Set LayoutManager
        when(orientation){
            Configuration.ORIENTATION_PORTRAIT -> mMoviesRecycler.layoutManager = GridLayoutManager(getApplicationContext(),2)
            Configuration.ORIENTATION_LANDSCAPE -> mMoviesRecycler.layoutManager = GridLayoutManager(getApplicationContext(),3)
        }



        mAdapter.mContext = getApplicationContext()
        //click Listener
        mMoviesRecycler.addOnItemTouchListener(
            RecyclerTouchListener(
                getApplicationContext(),
                mMoviesRecycler,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View, position: Int) {
                        val movie = viewModel.getTopRatedMovies().value!![position]
                        Toast.makeText(getApplicationContext(), movie.title + " is selected!", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.selectMovie(movie)

                    }

                    override fun onLongClick(view: View, position: Int) {

                    }
                })
        )

        //scroll Listener
        mMoviesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!mMoviesRecycler.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ){
                    Log.d(TOP_RATED_LIST_TAG,"Reached Bottom")
                    mMoviesRecycler.removeOnScrollListener(this)
                    viewModel.loadTopRatedMovies()
                    mMoviesRecycler.addOnScrollListener(this)
                }

            }
        })

        //Add RecyclerView Divider
        val horizontalDivider = DividerItemDecoration(mMoviesRecycler.context, DividerItemDecoration.HORIZONTAL)
        val verticalDivider = DividerItemDecoration(mMoviesRecycler.context, DividerItemDecoration.VERTICAL)
        mMoviesRecycler.addItemDecoration(horizontalDivider)
        mMoviesRecycler.addItemDecoration(verticalDivider)

        //Set adapter
        mMoviesRecycler.adapter = mAdapter
        //Set ItemAnimator
        mMoviesRecycler.itemAnimator = DefaultItemAnimator()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        viewModel.getTopRatedMovies().observe(this, Observer<ArrayList<Movie>> {
            mAdapter.setMovieList(viewModel.getTopRatedMovies().value!!)
            mAdapter.setTotalResults(viewModel.getTotalTopRatedMovies())
            val spinner = view.findViewById<ProgressBar>(R.id.list_spinner)
            if (viewModel.getTopRatedMovies().value == null) {
                spinner.visibility = View.VISIBLE
            }else{
                spinner.visibility = View.GONE
                mAdapter.notifyDataSetChanged()

            }
        })
    }



}
