package com.example.kitto.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import com.example.kitto.R
import com.example.kitto.data.Movie

class SearchableActivity : AppCompatActivity(){
    private val fragmentTag  = "SEARCH_FRAGMENT"
    private lateinit var viewModel: ListViewModel
    private var searchFragment = SearchableFragment()
    private var query = ""
    private lateinit var searchView:SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        // Verify the action and get the query
        viewModel.getSelectedMovie().observe(this, Observer<Movie?>{
            println("Observer called")
            if(viewModel.getSelectedMovie().value != null){
                viewModel.selectMovie(null)
                startActivity(Intent(applicationContext, MovieDetailsActivity::class.java))
                searchView.clearFocus()

            }else{
                println("Movie value is null")
            }

        })
        query = viewModel.getQuery()
        if(query == ""){
            query = intent.getStringExtra("QUERY")

        }
        viewModel.searchMovie(query)

        replaceSearchFragment()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.onActionViewExpanded()
        searchView.setQuery(query.replace("%20"," "),false)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchMovie(query?:" ")
                searchView.clearFocus()
                //searchFragment = SearchableFragment()
                searchFragment.restartScrollListener()
                replaceSearchFragment()

                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun replaceSearchFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.search_container,searchFragment,fragmentTag)
            .commit()
    }

    override fun onBackPressed() {
        viewModel.clearQuery()
        super.onBackPressed()
    }
}