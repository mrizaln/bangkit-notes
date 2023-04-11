package com.dicoding.picodiploma.myherolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.myherolist.model.Hero

class HeroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero)
        setupData()
    }

    private fun setupData() {
        val hero = intent.getParcelableExtra<Hero>("Hero") ?: throw IllegalStateException("this should not be null!")
        Glide.with(applicationContext)
            .load(hero.photo)
            .circleCrop()
            .into(findViewById(R.id.profileImageView))
        findViewById<TextView>(R.id.nameTextView).text = hero.name
        findViewById<TextView>(R.id.descTextView).text = hero.description
    }
}