package com.example.afnews.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase(){

    abstract fun newsDao(): NewsDao

}