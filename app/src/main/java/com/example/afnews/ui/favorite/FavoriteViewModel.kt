package com.example.afnews.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afnews.data.Article
import com.example.afnews.data.ArticleEntity
import com.example.afnews.data.repository.NewsOfflineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val offlineRepository: NewsOfflineRepository) :
    ViewModel() {

    private val _favoriteUiState = MutableStateFlow(FavoriteUiState(emptyList()))
    val favoriteUiState: StateFlow<FavoriteUiState> = _favoriteUiState

    init {
        getAllSavedNews()
    }

    private fun getAllSavedNews() {
        viewModelScope.launch(Dispatchers.IO) {
            offlineRepository.getAllNewsStream().map {
                _favoriteUiState.value = FavoriteUiState(it)
            }.stateIn(
                scope = viewModelScope
            )
        }
    }

    fun deleteFromSaved(articleEntity: ArticleEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            offlineRepository.deleteNews(articleEntity)
        }
    }
}


data class FavoriteUiState(val newsList: List<ArticleEntity> = listOf())