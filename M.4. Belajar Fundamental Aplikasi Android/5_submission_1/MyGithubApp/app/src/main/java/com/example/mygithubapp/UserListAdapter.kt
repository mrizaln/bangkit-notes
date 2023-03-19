package com.example.mygithubapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubapp.databinding.ItemRowUserBinding

class UserListAdapter(
    private var listUser: ArrayList<UserListData>,
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        if (user.name != null) {
            holder.binding.tvUsername.text = user.name
            holder.binding.cardUser.setOnClickListener {
                val userDetailIntent = Intent(holder.itemView.context, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, user.name)
                holder.itemView.context.startActivity(userDetailIntent)
            }
        } else {
            holder.binding.tvUsername.text = "-- null --"
        }

        if (user.avatar != null) {
            Glide.with(holder.itemView.context).load(user.avatar).into(holder.binding.imgUser)
        }
    }
}