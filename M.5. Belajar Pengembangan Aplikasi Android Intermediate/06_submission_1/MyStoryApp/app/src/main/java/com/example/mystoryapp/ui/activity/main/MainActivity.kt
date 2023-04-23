package com.example.mystoryapp.ui.activity.main

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystoryapp.R
import com.example.mystoryapp.data.di.ViewModelFactory
import com.example.mystoryapp.data.model.StoryModel
import com.example.mystoryapp.data.repository.RequestResult
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.ui.activity.login.LoginActivity
import com.example.mystoryapp.ui.activity.storyadd.StoryAddActivity
import com.example.mystoryapp.ui.activity.storydetail.StoryDetailActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private lateinit var listAdapter: StoryListAdapter

    private val launcherIntentAssStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.resetPagination()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        initializeSession()

        binding.layoutRefresh.setOnRefreshListener {
            viewModel.resetPagination()
            binding.layoutRefresh.isRefreshing = false
        }

        binding.fabAddStory.setOnClickListener {
            launcherIntentAssStory.launch(Intent(this, StoryAddActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout          -> {
                val dialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_logout_title))
                    .setMessage(getString(R.string.dialog_logout_message))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.dialog_confirm)) { _, _ ->
                        viewModel.logout()
                        Toast.makeText(
                            this, getString(R.string.menu_logout_toast), Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    .setNegativeButton(getString(R.string.dialog_reject)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                dialog.show()
            }
            R.id.action_change_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeSession() {
        viewModel.getUserData().observe(this) { userData ->
            if (userData == null) {

                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return@observe
            }

            viewModel.holdToken(userData.token)

            supportActionBar?.title = getString(R.string.activity_main_title)
            supportActionBar?.show()

            configureRecyclerView()
            configureStoryListObserver()

            if (listAdapter.itemCount == 0) {
                viewModel.loadMoreStories()
            }
        }
    }

    private fun configureRecyclerView() {
        val scrollOrientation = when (resources.configuration.orientation) {
            ORIENTATION_PORTRAIT  -> LinearLayoutManager.VERTICAL
            ORIENTATION_LANDSCAPE -> LinearLayoutManager.HORIZONTAL
            else                  -> LinearLayoutManager.VERTICAL
        }
        binding.rvStories.layoutManager = LinearLayoutManager(this, scrollOrientation, false)

        val onClickListener = object : StoryListAdapter.OnClickListener {
            override fun onClick(story: StoryModel) {
                val intent = Intent(this@MainActivity, StoryDetailActivity::class.java)
                intent.putExtra(StoryDetailActivity.EXTRA_STORY_ID, story.id)
                startActivity(intent)
            }
        }

        listAdapter = StoryListAdapter(onClickListener)
        binding.rvStories.adapter = listAdapter

        binding.rvStories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var scrollUp = false
            private var scrollRight = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager = recyclerView.layoutManager as LinearLayoutManager
                val adapter = recyclerView.adapter as StoryListAdapter

                val first = manager.findFirstVisibleItemPosition()
                val last = manager.findLastVisibleItemPosition()

                Log.d(
                    "OnScrollListener",
                    "state: $newState, scrollUp: $scrollUp, scrollRight: $scrollRight, first: $first, last: $last"
                )

                if ((scrollUp || scrollRight) && (last == adapter.itemCount - 1) && (newState == RecyclerView.SCROLL_STATE_IDLE))
                    viewModel.loadMoreStories()
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollUp = dy > 0
                scrollRight = dx > 0
            }
        })
    }

    private fun configureStoryListObserver() {
        viewModel.stories.observe(this) { result ->
            when (result) {
                is RequestResult.Loading -> {
                    showLoading(true)
                }
                is RequestResult.Error   -> {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.toast_main_load_failed, result.error),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    showLoading(false)
                }
                is RequestResult.Success -> {
                    showLoading(false)
                    val stories = result.data
                    listAdapter.replaceList(stories)
                    Log.d("storiesObserver", "stories size: ${stories.size}")
                }
            }
        }
    }

    private fun showLoading(visible: Boolean) {
        binding.pbStories.visibility = if (visible) View.VISIBLE else View.GONE
        binding.pbStories.isIndeterminate = true
    }
}