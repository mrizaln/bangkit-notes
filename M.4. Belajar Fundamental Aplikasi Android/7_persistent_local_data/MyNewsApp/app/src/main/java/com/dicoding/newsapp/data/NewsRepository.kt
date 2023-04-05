package com.dicoding.newsapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.newsapp.BuildConfig
import com.dicoding.newsapp.data.source.local.entity.NewsEntity
import com.dicoding.newsapp.data.source.local.room.NewsDao
import com.dicoding.newsapp.data.source.remote.retrofit.ApiService
import com.dicoding.newsapp.utils.AppExecutors

class NewsRepository private constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val appExecutors: AppExecutors,
) {
    companion object {
        @Volatile
        private var INSTANCE: NewsRepository? = null

        fun getInstance(
            apiService: ApiService,
            newsDao: NewsDao,
            appExecutors: AppExecutors,
        ): NewsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = NewsRepository(apiService, newsDao, appExecutors)
                INSTANCE!!
            }
        }
    }

    private val result = MediatorLiveData<RequestResult<List<NewsEntity>>>()

    fun getHeadlineNews(): LiveData<RequestResult<List<NewsEntity>>> = liveData {
        emit(RequestResult.Loading)
        try {
            val response = apiService.getNews(BuildConfig.API_KEY)
            val articles = response.articles
            val newsList = articles.map { article ->
                val isBookmarked = newsDao.isNewsBookmarked(article.title)
                NewsEntity(
                    article.title,
                    article.publishedAt,
                    article.urlToImage,
                    article.url,
                    isBookmarked
                )
            }
            newsDao.deleteAll()
            newsDao.insertNews(newsList)
        } catch (e: Exception) {
            Log.d(this@NewsRepository::class.simpleName, "${::getHeadlineNews.name}: ${e.message.toString()}")
            emit(RequestResult.Error(e.message.toString()))
        }

        val localData: LiveData<RequestResult<List<NewsEntity>>> = newsDao.getNews().map { RequestResult.Success(it) }
        emitSource(localData)
    }

    fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
        return newsDao.getBookmarkedNews()
    }

    suspend fun setBookmarkedNews(news: NewsEntity, bookmarkState: Boolean) {
        news.isBookmarked = bookmarkState
        newsDao.updateNews(news)
    }
}