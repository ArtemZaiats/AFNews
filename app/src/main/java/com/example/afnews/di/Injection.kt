package com.example.afnews.di

import android.content.Context
import androidx.room.Room
import com.example.afnews.data.NewsDao
import com.example.afnews.data.NewsDatabase
import com.example.afnews.data.repository.NewsOfflineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(context, NewsDatabase::class.java, "news_database")
            .build()
    }

    @Provides
    @Singleton
    fun providesDao(appDatabase: NewsDatabase) : NewsDao = appDatabase.newsDao()

    @Provides
    @Singleton
    fun provideOfflineRepository(newsDao: NewsDao): NewsOfflineRepository = NewsOfflineRepository(newsDao)

}