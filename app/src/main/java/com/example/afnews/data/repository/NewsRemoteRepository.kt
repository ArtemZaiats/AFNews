package com.example.afnews.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.afnews.data.network.NewsApiService
import com.example.afnews.data.network.NewsPagingSource
import javax.inject.Inject

class NewsRemoteRepository @Inject constructor(private val apiService: NewsApiService) {

    suspend fun getAllNewsStream(sortBy: String) = Pager(
        config = PagingConfig(pageSize = 10, initialLoadSize = 2),
        pagingSourceFactory = {
            NewsPagingSource(apiService, { apiService.getAllNews(sortBy = sortBy) })
        }
    )

    suspend fun searchNews(search: String) = Pager(
        config = PagingConfig(pageSize = 10, initialLoadSize = 2),
        pagingSourceFactory = {
            NewsPagingSource(apiService, { apiService.searchNews(query = search) })
        }
    )

    suspend fun getNewsByCategory(category: String) = Pager(
        config = PagingConfig(pageSize = 10, initialLoadSize = 2),
        pagingSourceFactory = {
            NewsPagingSource(apiService, { apiService.getAllNewsByCategory(category) })
        }
    )
}
