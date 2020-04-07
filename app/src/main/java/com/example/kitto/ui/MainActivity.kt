package com.example.kitto.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.facebook.login.LoginManager
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.example.kitto.BuildConfig
import com.example.kitto.R
import com.example.kitto.data.AccountRepository
import com.example.kitto.data.Movie
import com.facebook.*
import org.json.JSONObject

import com.example.kitto.ui.ListViewModel as ListViewModel




class MainActivity : AppCompatActivity() {


    private val LIST_COLLECTION_FRAGMENT = "LIST_COLLECTION_FRAGMENT"
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private var listFragment = MovieListFragment()
    private var collectionFragment = ListCollectionFragment()
    private val appIdFacebook = BuildConfig.APP_ID_FACEBOOK;
    private val MOVIE_LIST_FRAGMENT = "MOVIE_LIST_FRAGMENT"

    private lateinit var viewModel:ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        println("on Create Called Main")
        super.onCreate(savedInstanceState)
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if(!isLoggedIn){
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.elevation = 0F

        if(savedInstanceState == null){
            listFragment = MovieListFragment()
            swapFragments(LIST_COLLECTION_FRAGMENT)
        }
        //Sets view model
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.getListName().observe(this, Observer<Int>{
            swapFragments(LIST_COLLECTION_FRAGMENT)
        })

        viewModel.getSelectedMovie().observe(this, Observer<Movie?>{
            println("Observer called")
            if(viewModel.getSelectedMovie().value != null){
                viewModel.selectMovie(null)
                startActivity(Intent(applicationContext, MovieDetailsActivity::class.java))

            }else{
                println("Movie value is null")
            }

        })



        drawerLayout = findViewById(R.id.activity_main)
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout,
            R.string.Open,
            R.string.Close
        )


        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigationView = findViewById(R.id.nv)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.account -> startActivity(Intent(applicationContext, AccountActivity::class.java))

                R.id.log_out -> {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
                            AccessToken.setCurrentAccessToken(null)
                            LoginManager.getInstance().logOut()
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                        }).executeAsync()
                    }
                }
            }
            drawerLayout.closeDrawers()
            true

        }

    }
    private fun swapFragments(fragmentTag:String){
        when(fragmentTag){
            MOVIE_LIST_FRAGMENT -> supportFragmentManager.beginTransaction()
                .replace(R.id.list_container,listFragment,fragmentTag)
                .commit()
            LIST_COLLECTION_FRAGMENT -> supportFragmentManager.beginTransaction()
                .replace(R.id.list_container,collectionFragment,fragmentTag)
                .commit()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (drawerToggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)

    }

    override fun onSupportNavigateUp(): Boolean {

        if(!drawerLayout.isDrawerOpen(Gravity.START))drawerLayout.openDrawer(Gravity.START)
        else drawerLayout.closeDrawers()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(applicationContext, SearchableActivity::class.java)
                intent.putExtra("QUERY", query)
                startActivity(intent)
                searchView.clearFocus()
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}







