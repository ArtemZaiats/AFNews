package com.example.afnews.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.afnews.data.Article
import com.example.afnews.data.NewsResponse

class NewsPagingSource(
    private val apiService: NewsApiService,
    private val fetchFunction: suspend () -> NewsResponse
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = fetchFunction()
            LoadResult.Page(
                data = response.articles,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (response.articles.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}