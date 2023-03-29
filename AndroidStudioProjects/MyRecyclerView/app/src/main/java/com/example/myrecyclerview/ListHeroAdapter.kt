package com.example.myrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrecyclerview.databinding.ItemRowHeroBinding


class ListHeroAdapter(
    private val listHero: ArrayList<Hero>
) : RecyclerView.Adapter<ListHeroAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val view: View = LayoutInflater
//            .from(parent.context)
//            .inflate(R.layout.item_row_hero, parent, false)
//        return ListViewHolder(view)
        val binding = ItemRowHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, photo) = listHero[position]
        holder.apply {
//            imgPhoto.setImageResource(photo)
//            Glide.with(this.itemView.context)
//                .load(photo)                // URL gambar
//                .into(this.imgPhoto)        // imageViewm mana yang akan diterapkan
//            tvName.text = name
//            tvDescription.text = description

            Glide.with(this.itemView.context)
                .load(photo)                // URL gambar
                .into(this.binding.imgItemPhoto)        // imageViewm mana yang akan diterapkan
            binding.tvItemName.text = name
            binding.tvItemDescription.text = description
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listHero[holder.adapterPosition])
            }

//            itemView.setOnClickListener {
//                Toast.makeText(
//                    this.itemView.context,
//                    "Kamu memilih " + listHero[holder.adapterPosition].name,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
        }
    }

    override fun getItemCount(): Int = listHero.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Hero)
    }

//    class ListViewHolder(
//        itemView: View
//    ) : RecyclerView.ViewHolder(itemView) {
//        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
//        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
//        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
//    }
    class ListViewHolder(var binding: ItemRowHeroBinding) : RecyclerView.ViewHolder(binding.root)
}