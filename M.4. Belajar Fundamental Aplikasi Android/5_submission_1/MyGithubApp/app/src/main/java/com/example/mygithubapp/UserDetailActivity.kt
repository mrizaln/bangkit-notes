package com.example.mygithubapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubapp.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USERNAME = "username"

        enum class Follow(val res: Int) {
            FOLLOWERS(R.string.tab_followers),
            FOLLOWING(R.string.tab_following),
        }
    }

    private lateinit var binding: ActivityUserDetailBinding
    private val userDetailViewModel by viewModels<UserDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "User Detail"

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            userDetailViewModel.getUserDetail(username)
            setTabLayout(username)
        } else {
            Toast.makeText(this, "Failed to load data: null username value", Toast.LENGTH_SHORT)
                .show()
        }

        userDetailViewModel.isSuccessLoadUser.observe(this) { isSuccess ->
            if (!isSuccess) {
                Toast.makeText(
                    this, "Failed to load data", Toast.LENGTH_LONG
                ).show()
            }
        }

        userDetailViewModel.userData.observe(this) { response ->
            setUserData(response)
        }

        userDetailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBarProfileDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

    }

    private fun setUserData(userDetailResponse: UserDetailResponse) {
        binding.tvFullNameDetail.apply {
            text = userDetailResponse.name; visibility = TextView.VISIBLE
        }
        binding.tvUsernameDetail.apply {
            text = userDetailResponse.login; visibility = TextView.VISIBLE
        }
        binding.tvFollowers.apply {
            text = this@UserDetailActivity.resources.getString(
                R.string.followers, userDetailResponse.followers
            ); visibility = TextView.VISIBLE
        }
        binding.tvFollowing.apply {
            text = this@UserDetailActivity.resources.getString(
                R.string.following, userDetailResponse.following
            ); visibility = TextView.VISIBLE
        }

        Glide.with(this).load(userDetailResponse.avatarUrl).into(binding.imgUserProfileDetail)
    }

    private fun setTabLayout(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(Follow.values()[position].res)
        }.attach()

        supportActionBar?.elevation = 0f
    }
}