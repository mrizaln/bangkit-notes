package com.dicoding.newsapp.utils

import com.dicoding.newsapp.data.local.entity.NewsEntity
import com.dicoding.newsapp.data.remote.response.ArticlesItem
import com.dicoding.newsapp.data.remote.response.NewsResponse
import com.dicoding.newsapp.data.remote.response.Source

object DataDummy {
    fun generateDummyNewsEntity(): List<NewsEntity> = (0..10).map { i ->
        NewsEntity(
            "Title $i",
            "2022-02-22T22:22:22Z",
            "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
            "https://www.dicoding.com/",
        )
    }

    fun generateDummyNewsResponse(): NewsResponse {
        val newsList = (0..10).map { i ->
            ArticlesItem(
                "2022-02-22T22:22:22Z",
                "author $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "description $i",
                Source("name", "id"),
                "Title $i",
                "https://www.dicoding.com/",
                "content $i",
            )
        }
        return NewsResponse(newsList.size, newsList, "Success")
    }
}