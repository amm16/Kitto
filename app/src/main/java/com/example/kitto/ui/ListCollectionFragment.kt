package com.example.kitto.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.kitto.R




class ListCollectionFragment : Fragment() {
    private lateinit var fragmentAdapter: FragmentPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var viewModel:ListViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_list_collection, container, false)
        var spinner = view.findViewById<ProgressBar>(R.id.list_spinner)
        view.findViewById<ProgressBar>(R.id.list_spinner)?.visibility = View.GONE
        fragmentAdapter = FragmentPagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = fragmentAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.getCurrentPage().observe(this, Observer<Int> {
            viewPager.currentItem = viewModel.getCurrentPage().value ?:0
        })

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.selectPage(viewPager.currentItem)
    }






}
