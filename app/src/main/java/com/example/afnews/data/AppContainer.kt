package com.example.afnews.data


//interface AppContainer {
//    val remoteRepository: NewsRemoteRepository
//    val offlineRepository: NewsOfflineRepository
//}
//
//class AppDataContainer(private val context: Context) : AppContainer {
//
//    override val remoteRepository: NewsRemoteRepository by lazy {
//        NewsRemoteRepository(RetrofitClient.provideRetrofitService())
//    }
//
//    override val offlineRepository: NewsOfflineRepository by lazy {
//        NewsOfflineRepository(NewsDatabase.getDatabase(context).newsDao())
//    }
//
//}