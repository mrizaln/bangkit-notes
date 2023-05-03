package com.example.mystoryapp2.model.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = RemoteKey.TABLE_NAME)
data class RemoteKey(
    @PrimaryKey
    val id: String,

    val prevKey: Int?,
    val nextKey: Int?,
) {
    companion object {
        const val TABLE_NAME = "remote_key"
    }
}