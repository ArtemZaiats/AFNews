package com.example.afnews.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.afnews.data.repository.NewsOfflineRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val offlineRepository: NewsOfflineRepository) :
    ViewModel() {

}