package com.example.mystoryapp2.model.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<Story>)

    @Query("SELECT * FROM ${Story.TABLE_NAME}")
    fun getStories(): PagingSource<Int, Story>

    @Query("DELETE FROM ${Story.TABLE_NAME}")
    suspend fun deleteAll()
}