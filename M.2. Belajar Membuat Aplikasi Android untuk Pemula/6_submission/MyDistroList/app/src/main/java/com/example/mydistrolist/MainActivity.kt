package com.example.mydistrolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydistrolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<Distro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvDistros.setHasFixedSize(true)
        list.addAll(getDistros())
        showRecyclerList()

        supportActionBar?.title = "Linux Distros"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_page -> {
                val aboutIntent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDistros(): ArrayList<Distro> {
        val (names, descriptions, details, photos) = arrayOf(
            R.array.data_distro, R.array.data_description, R.array.data_detail, R.array.data_photo
        ).map {
            resources.getStringArray(it)
        }

        val distroList = ArrayList<Distro>()
        for (i in names.indices) {
            val (logo, desktopPreview) = photos[i].split(';')
            distroList.add(Distro(names[i], descriptions[i], details[i], logo, desktopPreview))
        }
        return distroList
    }

    private fun showRecyclerList() {
        binding.rvDistros.layoutManager = LinearLayoutManager(this)
        val listDistroAdapter = ListDistroAdapter(list)
        binding.rvDistros.adapter = listDistroAdapter
    }
}