package com.example.mynotesapp2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteRoomDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): NoteRoomDatabase {
            return INSTANCE ?: synchronized(NoteRoomDatabase::class.java) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE!!
            }
        }
    }

    abstract fun noteDao(): NoteDao
}