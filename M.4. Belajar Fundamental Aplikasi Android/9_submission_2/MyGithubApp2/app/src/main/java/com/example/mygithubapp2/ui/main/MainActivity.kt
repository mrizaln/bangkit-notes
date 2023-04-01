package com.example.mygithubapp2.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapp2.R
import com.example.mygithubapp2.data.local.entity.UserEntity
import com.example.mygithubapp2.data.remote.RequestResult
import com.example.mygithubapp2.databinding.ActivityMainBinding
import com.example.mygithubapp2.ui.UserListAdapter
import com.example.mygithubapp2.ui.ViewModelFactory
import com.example.mygithubapp2.ui.favorite.FavoriteActivity
import com.example.mygithubapp2.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Users"

        mainViewModel.listUser.observe(this) { result ->
            when (result) {
                is RequestResult.Loading -> binding.progressBarMain.visibility = View.VISIBLE
                is RequestResult.Success -> {
                    binding.progressBarMain.visibility = View.GONE
                    setUserList(result.data)
                }
                is RequestResult.Error -> {
                    binding.progressBarMain.visibility = View.GONE
                    Toast.makeText(
                        this, "Failed to load data: ${result.error}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        mainViewModel.getThemeSetting().observe(this) { isDarkMode ->
            if (isDarkMode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.search_hint)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    binding.rvUsers.adapter = null
                    mainViewModel.search(query.toString())
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean = false
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite_list -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserList(listUser: List<UserEntity>) {
        if (listUser.isEmpty()) {
            Toast.makeText(
                this, "Term ${mainViewModel.queryTerm.value} returns nothing", Toast.LENGTH_SHORT
            ).show()
        }

        val adapter = UserListAdapter(listUser)
        binding.rvUsers.adapter = adapter
    }
}