package com.example.mydeepnavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mydeepnavigation.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    enum class Extra(val string: String) {
        TITLE("extra_title"),
        MESSAGE("extra_message"),
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(Extra.TITLE.string) ?: ""
        val message = intent.getStringExtra(Extra.MESSAGE.string) ?: ""

        binding.tvTitle.text = title
        binding.tvMessage.text = message
    }
}