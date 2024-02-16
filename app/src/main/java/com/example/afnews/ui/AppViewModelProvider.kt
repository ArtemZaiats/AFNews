package com.example.afnews.ui

//object AppViewModelProvider {
//    val Factory = viewModelFactory {
//        initializer {
//            HomeViewModel(
//                newsApplication().container.remoteRepository,
//                newsApplication().container.offlineRepository
//            )
//        }
//        initializer {
//            FavoriteViewModel(newsApplication().container.offlineRepository)
//        }
//    }
//}
//
//fun CreationExtras.newsApplication(): NewsApplication =
//    (this[AndroidViewModelFactory.APPLICATION_KEY] as NewsApplication)