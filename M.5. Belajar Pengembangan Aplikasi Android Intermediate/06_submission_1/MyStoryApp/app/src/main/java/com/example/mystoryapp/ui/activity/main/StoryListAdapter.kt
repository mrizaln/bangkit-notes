package com.example.mystoryapp.ui.activity.main

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mystoryapp.R
import com.example.mystoryapp.data.model.StoryModel
import com.example.mystoryapp.databinding.ItemRowStoryBinding

class StoryListAdapter(
    private val onClickListener: OnClickListener,
) : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {
    private var storyList = listOf<StoryModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListAdapter.ViewHolder {
        val binding = ItemRowStoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = storyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = storyList[position]

        holder.binding.apply {
            cardStory.setOnClickListener {
                onClickListener.onClick(story)
            }
            tvItemName.text = story.name
            holder.loadImage(story.photoUrl)
        }
    }

//    fun emptyList() {
//        val size = storyList.size
//        storyList.clear()
//        notifyItemRangeRemoved(0, size)
//    }

    fun replaceList(list: List<StoryModel>) {
        storyList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        val binding: ItemRowStoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun loadImage(url: String) {
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
        fun onClick(story: StoryModel)
    }
}