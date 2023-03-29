package com.example.mynotesapp.helper

import android.database.Cursor
import com.example.mynotesapp.db.DatabaseContract.NoteColumns
import com.example.mynotesapp.entity.Note

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Note> {
        val notesList = ArrayList<Note>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(NoteColumns._ID))
                val title = getString(getColumnIndexOrThrow(NoteColumns.TITLE))
                val description = getString(getColumnIndexOrThrow(NoteColumns.DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(NoteColumns.DATE))
                notesList.add(Note(id, title, description, date))
            }
        }
        return notesList
    }
}