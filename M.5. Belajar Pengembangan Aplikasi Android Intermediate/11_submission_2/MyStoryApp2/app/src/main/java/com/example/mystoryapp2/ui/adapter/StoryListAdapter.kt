package com.example.mystoryapp2.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mystoryapp2.R
import com.example.mystoryapp2.databinding.ItemRowStoryBinding
import com.example.mystoryapp2.model.data.local.database.Story

class StoryListAdapter(
    private val onClickListener: OnClickListener,
) : PagingDataAdapter<Story, StoryListAdapter.ViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowStoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { story ->
            holder.bind(story)
        }

    }

    inner class ViewHolder(
        val binding: ItemRowStoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            binding.apply {
                cardStory.setOnClickListener {
                    onClickListener.onClick(story)
                }
                tvItemName.text = story.name

                loadImage(story.photoUrl)
            }
        }

        private fun loadImage(url: String) {
            val context = this.itemView.context
            val b = binding

            b.pbItem.visibility = View.VISIBLE

            val glideListener = object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    b.pbItem.visibility = View.GONE
                    b.ivItemPhoto.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.baseline_image_24)
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
                    b.pbItem.visibility = View.GONE
                    return false
                }
            }

            Glide.with(context)
                .load(url)
                .listener(glideListener)
                .into(binding.ivItemPhoto)
        }
    }

    interface OnClickListener {
        fun onClick(story: Story)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}