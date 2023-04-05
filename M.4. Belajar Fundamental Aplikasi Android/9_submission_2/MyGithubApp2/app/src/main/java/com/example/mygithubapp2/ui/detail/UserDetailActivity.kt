package com.example.mygithubapp2.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubapp2.R
import com.example.mygithubapp2.data.local.entity.UserEntity
import com.example.mygithubapp2.data.remote.RequestResult
import com.example.mygithubapp2.databinding.ActivityUserDetailBinding
import com.example.mygithubapp2.ui.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USERNAME = "username"
    }

    enum class Follow(val res: Int) {
        FOLLOWERS(R.string.tab_followers),
        FOLLOWING(R.string.tab_following),
    }

    private var userDetail: UserDetail? = null
    private var isFavorite = false

    private lateinit var binding: ActivityUserDetailBinding
    private val userDetailViewModel: UserDetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "User Detail"

        val username = intent.getStringExtra(EXTRA_USERNAME)!!
        setTabLayout(username)

        userDetailViewModel.getUserDetail(username).observe(this) { result ->
            when (result) {
                is RequestResult.Loading -> {
                    binding.progressBarProfileDetail.visibility = View.VISIBLE
                }
                is RequestResult.Success -> {
                    binding.progressBarProfileDetail.visibility = View.GONE
                    userDetail = result.data
                    setUserData(userDetail!!)
                }
                is RequestResult.Error -> {
                    binding.progressBarProfileDetail.visibility = View.GONE
//                    Toast.makeText(
//                        this, "Failed to load data: ${result.error}", Toast.LENGTH_LONG
//                    ).show()
                    showToast(this, "Failed to load data: ${result.error}")
                }
            }
        }

        userDetailViewModel.getFavoriteUserByUsername(username).observe(this) { result ->
            isFavorite = result != null
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)

        menu?.getItem(0)?.icon = resources.getDrawable(
            if (isFavorite)
                R.drawable.ic_favorite_24dp
            else
                R.drawable.ic_favorite_border_24dp
        )

        return super.onCreateOptionsMenu(menu)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // don't handle menu when userDetail still null
        if (userDetail == null)
            return super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.menu_favorite -> {
                item.icon = resources.getDrawable(
                    if (isFavorite) {
                        userDetailViewModel.removeFavoriteUserByUsername(userDetail!!.username)
                        showToast(this@UserDetailActivity, "${userDetail!!.username} removed from favorite")
                        R.drawable.ic_favorite_border_24dp
                    } else {
                        userDetailViewModel.addUserToFavorite(
                            UserEntity(userDetail!!.username, userDetail!!.avatarUrl)
                        )
                        showToast(this@UserDetailActivity, "${userDetail!!.username} added to favorite")
                        R.drawable.ic_favorite_24dp
                    }
                )
            }
            R.id.menu_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "https://github.com/${userDetail!!.username}")
                }
                startActivity(Intent.createChooser(shareIntent, "Share user"))
//                Toast.makeText(
//                    this@UserDetailActivity,
//                    "Sharing ${userDetail!!.fullName}",
//                    Toast.LENGTH_SHORT
//                ).show()
                showToast(this@UserDetailActivity, "Sharing ${userDetail!!.fullName}")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData(userDetail: UserDetail) {
        binding.tvFullNameDetail.apply {
            text = userDetail.fullName
            visibility = TextView.VISIBLE
        }
        binding.tvUsernameDetail.apply {
            text = userDetail.username
            visibility = TextView.VISIBLE
        }
        binding.tvFollowers.apply {
            text = this@UserDetailActivity.resources.getString(
                R.string.followers,
                userDetail.numOfFollowers
            )
            visibility = TextView.VISIBLE
        }
        binding.tvFollowing.apply {
            text = this@UserDetailActivity.resources.getString(
                R.string.following,
                userDetail.numOfFollowing
            )
            visibility = TextView.VISIBLE
        }

        Glide.with(this)
            .load(userDetail.avatarUrl)
            .into(binding.imgUserProfileDetail)
    }

    private fun setTabLayout(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(Follow.values()[position].res)
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText( context, message, Toast.LENGTH_SHORT).show()
    }
}