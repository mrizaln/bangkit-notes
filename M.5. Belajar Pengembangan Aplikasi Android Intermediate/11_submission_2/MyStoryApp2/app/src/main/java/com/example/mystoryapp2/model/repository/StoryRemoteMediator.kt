package com.example.mystoryapp2.model.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.mystoryapp2.model.data.local.database.RemoteKey
import com.example.mystoryapp2.model.data.local.database.Story
import com.example.mystoryapp2.model.data.local.database.StoryDatabase
import com.example.mystoryapp2.model.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
) : RemoteMediator<Int, Story>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKey = getRemoteKey(state, Position.FIRST)
                val prevKey = remoteKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                prevKey
            }

            LoadType.APPEND  -> {
                val remoteKey = getRemoteKey(state, Position.LAST)
                val nextKey = remoteKey?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                nextKey
            }
        }
        Log.d(javaClass.simpleName, "load page: $page")

        return try {
            val response = apiService.getStories(page, state.config.pageSize)
            val stories = response.listStory.map {
                Story(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    photoUrl = it.photoUrl,
                    createdAt = it.createdAt,
                    lon = it.lon,
                    lat = it.lat
                )
            }
            val isEndOfPaginationReached = stories.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeyDao().deleteAll()
                    database.storyDao().deleteAll()

                    Log.d(javaClass.simpleName, "load: refresh | page: $page")
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfPaginationReached) null else page + 1
                val keys = stories.map {
                    RemoteKey(
                        id = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                    )
                }

                database.remoteKeyDao().insertAll(keys)
                database.storyDao().insertAll(stories)
            }

            MediatorResult.Success(isEndOfPaginationReached)
        } catch (e: Exception) {
            Log.d(javaClass.simpleName, "load error: ${e.message}")
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKey(
        state: PagingState<Int, Story>,
        position: Position,
    ): RemoteKey? {
        return when (position) {
            Position.LAST    -> state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { data -> database.remoteKeyDao().getId(data.id) }

            Position.FIRST   -> state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { data -> database.remoteKeyDao().getId(data.id) }

            Position.CLOSEST -> state.anchorPosition?.let { pos ->
                state.closestItemToPosition(pos)?.id?.let { id ->
                    database.remoteKeyDao().getId(id)
                }
            }
        }
    }

    private enum class Position {
        FIRST, LAST, CLOSEST
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val PAGE_SIZE = 10
        const val PREFETCH_SIZE = 3
        const val MAX_SIZE = 250
    }
}