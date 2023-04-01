package com.example.mygithubapp2.ui.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapp2.R
import com.example.mygithubapp2.data.local.entity.UserEntity
import com.example.mygithubapp2.databinding.ActivityFavoriteBinding
import com.example.mygithubapp2.ui.UserListAdapter
import com.example.mygithubapp2.ui.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorites"

        favoriteViewModel.getUsers().observe(this) { result ->
            binding.progressBarFavorite.visibility = View.GONE
            setUserList(result)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUsers.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.favorite_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite_delete -> showDeleteAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserList(listUser: List<UserEntity>) {
        if (listUser.isEmpty()) {
            Toast.makeText(this, "You haven't set any user as favorite", Toast.LENGTH_SHORT).show()
        }

        val adapter = UserListAdapter(listUser)
        binding.rvFavoriteUsers.adapter = adapter
    }

    private fun showDeleteAlertDialog() {
        val dialogTitle = "Delete All"
        val dialogMessage = "Are you sure you want to delete all favorites?"

        val dialog = AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                favoriteViewModel.removeUsers()
                Toast.makeText(this, "Deleted all favorites", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            .create()
        dialog.show()
    }

}
