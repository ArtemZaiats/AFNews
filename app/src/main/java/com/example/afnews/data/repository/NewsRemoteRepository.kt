package com.example.afnews.data.repository

import com.example.afnews.data.NewsResponse
import com.example.afnews.data.network.BaseApiResponse
import com.example.afnews.data.network.NetworkResult
import com.example.afnews.data.network.NewsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

//class NewsRemoteRepository(private val apiService: NewsApiService) {
class NewsRemoteRepository @Inject constructor(private val apiService: NewsApiService) :
    BaseApiResponse() {

    fun getAllNewsStream(): Flow<NetworkResult<NewsResponse>> = flow {
        emit(safeApiCall { apiService.getAllNews() })
    }

    suspend fun searchNews(search: String): Flow<NetworkResult<NewsResponse>> = flow {
        emit(safeApiCall { apiService.searchNews(query = search) })
    }

    suspend fun getNewsByCategory(category: String): Response<NewsResponse> {
        return apiService.getAllNewsByCategory(category)
    }

}