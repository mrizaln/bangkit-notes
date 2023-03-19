package com.example.mygithubapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(
    activity: AppCompatActivity,
) : FragmentStateAdapter(activity) {
    var username: String = ""

    override fun getItemCount(): Int = UserDetailActivity.Companion.Follow.values().size

    override fun createFragment(position: Int): Fragment {
        val fragment = UserDetailFollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(UserDetailFollowFragment.ARG_POSITION, position + 1)
            putString(UserDetailFollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }

}