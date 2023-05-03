package com.example.mystoryapp2.ui.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mystoryapp2.R
import com.example.mystoryapp2.databinding.ActivityStoryDetailBinding
import com.example.mystoryapp2.model.data.local.database.Story
import com.example.mystoryapp2.model.repository.RequestResult
import com.example.mystoryapp2.ui.viewmodel.StoryDetailViewModel
import com.example.mystoryapp2.ui.viewmodel.ViewModelFactory

class StoryDetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityStoryDetailBinding.inflate(layoutInflater) }
    private val viewModel: StoryDetailViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getStringExtra(EXTRA_STORY_ID)?.let { storyId ->
            viewModel.getStory(storyId).observe(this) { result ->
                when (result) {
                    is RequestResult.Loading -> {
                        showLoading(true)
                    }

                    is RequestResult.Error   -> {
                        onFailedToLoad(
                            getString(
                                R.string.toast_story_detail_load_failed,
                                result.error
                            )
                        )
                        showLoading(false)
                    }

                    is RequestResult.Success -> {
                        loadStory(result.data)
                        showLoading(false)
                    }
                }
            }
        } ?: onFailedToLoad(getString(R.string.toast_story_detail_load_failed, "null id"))

        supportActionBar?.title = getString(R.string.activity_story_detail_title)
    }

    private fun loadStory(story: Story, loadImage: Boolean = true) {
        val b = binding

        b.pbDetail.visibility = View.VISIBLE
        b.tvDetailName.text = story.name
        b.tvDetailDescription.text = story.description

        val glideListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                b.pbDetail.visibility = View.GONE
                b.ivDetailPhoto.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@StoryDetailActivity, R.drawable.baseline_image_24
                    )
                )
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                b.pbDetail.visibility = View.GONE
                return false
            }

        }

        if (loadImage) {
            Glide.with(this).load(story.photoUrl).listener(glideListener).into(b.ivDetailPhoto)
        }
    }

    private fun onFailedToLoad(toastMessage: String) {
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        loadStory(Story("", "null", "null", "null", "null"), false)
    }

    private fun showLoading(visible: Boolean) {
        binding.pbDetail.visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_STORY_ID = "com.example.mystoryapp2.ui.activity.storydetail.EXTRA_STORY_ID"
    }
}