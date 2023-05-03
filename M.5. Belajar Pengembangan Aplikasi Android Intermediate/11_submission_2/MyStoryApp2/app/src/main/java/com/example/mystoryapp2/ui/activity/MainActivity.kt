package com.example.mystoryapp2.ui.activity

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystoryapp2.R
import com.example.mystoryapp2.databinding.ActivityMainBinding
import com.example.mystoryapp2.model.data.local.database.Story
import com.example.mystoryapp2.ui.adapter.LoadingStateAdapter
import com.example.mystoryapp2.ui.adapter.StoryListAdapter
import com.example.mystoryapp2.ui.viewmodel.MainViewModel
import com.example.mystoryapp2.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private lateinit var listAdapter: StoryListAdapter

    private val launcherIntentAddStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.d(javaClass.simpleName, "launcherIntentAddStory: RESULT_OK")
            listAdapter.refresh()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        initializeSession()

        binding.layoutRefresh.setOnRefreshListener {
            listAdapter.refresh()
        }

        binding.fabAddStory.setOnClickListener {
            launcherIntentAddStory.launch(Intent(this, StoryAddActivity::class.java))
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
                        ).show()
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

            R.id.action_view_maps       -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }

            R.id.action_refresh         -> {
                binding.rvStories.smoothScrollToPosition(0)
                listAdapter.refresh()
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

            binding.pbStories.visibility = View.GONE

            configureRecyclerView()
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
            override fun onClick(story: Story) {
                val intent = Intent(this@MainActivity, StoryDetailActivity::class.java)
                intent.putExtra(StoryDetailActivity.EXTRA_STORY_ID, story.id)
                startActivity(intent)
            }
        }

        listAdapter = StoryListAdapter(onClickListener)
        binding.rvStories.adapter = listAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter(
                listAdapter::retry
            )
        )

        listAdapter.addLoadStateListener { state ->
            Log.d(
                javaClass.simpleName,
                "loadStateListener: (refresh: ${state.refresh} | append: ${state.append} | prepend: ${state.prepend}"
            )

            when (state.refresh) {
                is LoadState.Error              -> {
                    val message = (state.refresh as LoadState.Error).error.message
                    setLoadingState(false)
                    Toast.makeText(
                        this,
                        getString(R.string.toast_main_load_failed, message),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is LoadState.Loading        -> {
                    setLoadingState(true)
                }

                is LoadState.NotLoading -> {
                    if (binding.layoutRefresh.isRefreshing)
                        setLoadingState(false)
                }
            }
        }

        listAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.rvStories.smoothScrollToPosition(0)
                }
            }
        })

        viewModel.stories.observe(this) { pagingData ->
            listAdapter.submitData(lifecycle, pagingData)
            Log.d(javaClass.simpleName, "listObserver: new data")
        }
    }

    private fun setLoadingState(loading: Boolean) {
        binding.layoutRefresh.isRefreshing = loading
    }
}