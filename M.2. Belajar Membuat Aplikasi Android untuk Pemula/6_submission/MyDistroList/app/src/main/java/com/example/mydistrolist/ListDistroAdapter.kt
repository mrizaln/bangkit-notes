package com.example.mydistrolist

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mydistrolist.databinding.ItemRowDistroBinding
import com.bumptech.glide.Glide

class ListDistroAdapter(
    private val distroList: ArrayList<Distro>
) : RecyclerView.Adapter<ListDistroAdapter.ListViewHolder>() {
    companion object {
        const val KEY_DISTRO: String = "key_hero"
    }

    class ListViewHolder(var binding: ItemRowDistroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowDistroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, _, logo, _) = distroList[position]
        Glide.with(holder.itemView.context)
            .load(logo)
            .into(holder.binding.imgItemPhoto)
        holder.apply {
            binding.tvItemTitle.text = name
            binding.tvItemDescription.text = description.split('.')[0]  // show only first sentence
            itemView.setOnClickListener {
                val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(KEY_DISTRO, distroList[holder.adapterPosition])
                holder.itemView.context.startActivity(intentDetail)
            }
        }
    }

    override fun getItemCount(): Int = distroList.size
}