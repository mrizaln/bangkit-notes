package com.dicoding.myunlimitedquotes.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.myunlimitedquotes.database.QuoteDatabase
import com.dicoding.myunlimitedquotes.network.ApiService
import com.dicoding.myunlimitedquotes.network.QuoteResponseItem

class QuoteRepository(
    private val quoteDatabase: QuoteDatabase, private val apiService: ApiService,
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getQuote(): LiveData<PagingData<QuoteResponseItem>> = Pager(
        config = PagingConfig(
            pageSize = 5
        ),
//        pagingSourceFactory = QuotePagingSource(apiService)::invoke       // << tanpa mediator
        remoteMediator = QuoteRemoteMediator(quoteDatabase, apiService),
        pagingSourceFactory = quoteDatabase.quoteDao()::getAllQuote
    ).liveData
}