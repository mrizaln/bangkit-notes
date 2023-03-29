package com.example.myquote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuoteAdapter(private val listView: ArrayList<String>) : RecyclerView.Adapter<QuoteAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.findViewById(R.id.tvItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listView.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItem.text = listView[position]
    }

}