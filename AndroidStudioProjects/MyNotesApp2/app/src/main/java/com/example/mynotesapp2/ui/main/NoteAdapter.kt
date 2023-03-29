package com.example.mynotesapp2.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotesapp2.database.Note
import com.example.mynotesapp2.databinding.ItemNoteBinding
import com.example.mynotesapp2.helper.NoteDiffCallback
import com.example.mynotesapp2.ui.insert.NoteAddUpdateActivity

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(
        private val binding: ItemNoteBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                tvItemTitle.text = note.title
                tvItemDate.text = note.date
                tvItemDescription.text = note.description
                cvItemNote.setOnClickListener {
                    val intent = Intent(it.context, NoteAddUpdateActivity::class.java)
                    intent.putExtra(NoteAddUpdateActivity.Extra.NOTE.string, note)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    private val listNotes = ArrayList<Note>()

    fun setListNotes(listNotes: List<Note>) {
        val diffCallback = NoteDiffCallback(this.listNotes, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listNotes.clear()
        this.listNotes.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }
}