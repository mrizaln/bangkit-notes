package com.example.mystoryapp2.model.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Story.TABLE_NAME)
data class Story(
    @PrimaryKey
    val id: String,

    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lon: Double? = null,
    val lat: Double? = null,
) {
    companion object {
        const val TABLE_NAME = "story"
    }
}