package com.example.mygithubapp2.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mygithubapp2.ui.detail.follow.UserDetailFollowFragment

class SectionsPagerAdapter(
    activity: AppCompatActivity,
    private val username: String
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = UserDetailActivity.Follow.values().size

    override fun createFragment(position: Int): Fragment {
        val fragment = UserDetailFollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(UserDetailFollowFragment.Arguments.POSITION.key, position + 1)
            putString(UserDetailFollowFragment.Arguments.USERNAME.key, username)
        }
        return fragment
    }

}