package com.example.kitto.ui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class FragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int  = 4

    override fun getItem(i: Int): Fragment {
        val fragment:Fragment
        when(i){
            0 -> fragment = PopularListFragment()
            1 -> fragment = PlayingNowListFragment()
            2 -> fragment = TopRatedListFragment()
            3 -> fragment = UpcomingListFragment()
            else -> fragment = PlayingNowListFragment()
        }

        return fragment

    }



    override fun getPageTitle(position: Int): String {
        var title = ""
        when(position){
            0-> title = "Popular"
            1-> title = "In Theaters"
            2-> title = "Top Rated"
            3-> title = "Upcoming"
        }
        return title
    }
}
