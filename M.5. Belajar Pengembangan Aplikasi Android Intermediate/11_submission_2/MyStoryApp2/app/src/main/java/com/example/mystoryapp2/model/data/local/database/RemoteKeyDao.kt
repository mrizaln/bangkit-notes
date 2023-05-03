package com.example.mystoryapp2.model.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM ${RemoteKey.TABLE_NAME} WHERE id = :id")
    suspend fun getId(id: String): RemoteKey?

    @Query("DELETE FROM ${RemoteKey.TABLE_NAME}")
    suspend fun deleteAll()
}