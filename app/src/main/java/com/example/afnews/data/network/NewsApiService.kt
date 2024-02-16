package com.example.afnews.data.network

import com.example.afnews.data.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("everything")
    suspend fun getAllNews(
        @Query("q") query: String = "all"
    ): Response<NewsResponse>

    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String = "all",
        @Query("sortBy") sortBy: String = "relevancy"
    ): Response<NewsResponse>

    @GET("top-headlines")
    suspend fun getAllNewsByCategory(
        @Query("category") category: String,
    ): Response<NewsResponse>
}