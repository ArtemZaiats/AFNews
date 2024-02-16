package com.example.afnews.data.repository

import com.example.afnews.data.ArticleEntity
import com.example.afnews.data.NewsDao
import javax.inject.Inject

class NewsOfflineRepository @Inject constructor(private val newsDao: NewsDao) {

    fun getAllNewsStream() = newsDao.getAllNews()

    suspend fun insertNews(news: ArticleEntity) = newsDao.insert(news)

    suspend fun deleteNews(news: ArticleEntity) = newsDao.delete(news)
}

