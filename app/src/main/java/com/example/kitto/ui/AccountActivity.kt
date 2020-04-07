package com.example.kitto.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.kitto.R




class AccountActivity : AppCompatActivity() {
    private lateinit var viewModel:AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val nameView :TextView =findViewById(R.id.name)
        val pictureView :ImageView = findViewById(R.id.profile_picture)
        val birthdayView :TextView = findViewById(R.id.birthday)
        val emailView :TextView = findViewById(R.id.email)
        //val genderView:TextView = findViewById(R.id.gender)


        //Sets view model
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        viewModel.lastName.observe(this, Observer<String>{
            println(viewModel.firstName.value)
            val fullName = viewModel.firstName.value + " " + viewModel.lastName.value
            nameView.text = fullName

        })

        viewModel.birthday.observe(this, Observer<String>{
            birthdayView.text = viewModel.birthday.value
        })

        viewModel.email.observe(this, Observer<String>{
            emailView.text = viewModel.email.value
        })

        viewModel.id.observe(this, Observer<String>{
            val imageURL = "https://graph.facebook.com/${viewModel.id.value}/picture?type=large"

            Glide.with(applicationContext)
                .load(imageURL)
                .circleCrop()
                .placeholder(R.drawable.ic_local_movies_dark_blue_24dp)
                .into(pictureView)
        })



    }

}
