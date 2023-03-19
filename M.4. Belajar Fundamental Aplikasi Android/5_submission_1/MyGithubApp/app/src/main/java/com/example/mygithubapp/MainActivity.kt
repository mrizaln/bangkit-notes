package com.example.mygithubapp

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github Users"

        mainViewModel.isSuccessLoadUsers.observe(this) { isSuccess ->
            if (!isSuccess) {
                Toast.makeText(
                    this, "Failed to load data", Toast.LENGTH_LONG
                ).show()
            }
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBarMain.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mainViewModel.listUser.observe(this) { listUserResponse ->
            setUserList(listUserResponse)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

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

    private fun setUserList(listUsersResponse: List<UserItemResponse?>) {
        val listUser = ArrayList<UserListData>()
        for (userResponse in listUsersResponse) {
            if (userResponse == null) continue

            val username = userResponse.login
            val avatar = userResponse.avatarUrl

            val userListData = UserListData(
                username, avatar
            )
            listUser.add(userListData)
        }
        if (listUser.size <= 0) {
            Toast.makeText(
                this, "Term ${mainViewModel.queryTerm.value} returns nothing", Toast.LENGTH_SHORT
            ).show()
            return
        }

        val adapter = UserListAdapter(listUser)
        binding.rvUsers.adapter = adapter
    }
}