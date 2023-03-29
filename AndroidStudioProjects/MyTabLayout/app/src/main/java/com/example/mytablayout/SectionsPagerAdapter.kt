package com.example.mytablayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(
    activity: AppCompatActivity,        // use FragmentActivity if using Fragment instead of Activity
) : FragmentStateAdapter(activity) {

    var appName: String = ""

    override fun createFragment(position: Int): Fragment {
//        var fragment: Fragment? = null
//        when (position) {
//            0 -> fragment = HomeFragment()
//            1 -> fragment = ProfileFragment()
//        }
        val fragment = HomeFragment()
        fragment.arguments = Bundle().apply {
            putInt(HomeFragment.ARG_SECTION_NUMBER, position + 1)
            putString(HomeFragment.ARG_NAME, appName)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int = 3
}