package com.example.mygithubapp2.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubapp2.data.local.entity.UserEntity
import com.example.mygithubapp2.databinding.ItemRowUserBinding
import com.example.mygithubapp2.ui.detail.UserDetailActivity

class UserListAdapter(
    private var listUser: List<UserEntity>,
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.binding.tvUsername.text = user.username

        holder.binding.cardUser.setOnClickListener {
            val userDetailIntent = Intent(holder.itemView.context, UserDetailActivity::class.java)
            userDetailIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, user.username)
            holder.itemView.context.startActivity(userDetailIntent)
        }

        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .into(holder.binding.imgUser)
    }
}