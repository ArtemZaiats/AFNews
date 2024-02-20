package com.example.afnews.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.afnews.data.Article
import com.example.afnews.data.ArticleEntity
import com.example.afnews.data.repository.NewsOfflineRepository
import com.example.afnews.data.repository.NewsRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: NewsRemoteRepository,
    private val offlineRepository: NewsOfflineRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<PagingData<Article>> =
        MutableStateFlow(value = PagingData.empty())
    val uiState: StateFlow<PagingData<Article>> = _uiState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        getAllNews()
    }

    fun getAllNews(sortBy: String = "publishedAt") {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            val response = remoteRepository.getAllNewsStream(sortBy = sortBy)
            response.flow.cachedIn(viewModelScope).collect {
                _uiState.value = it
                _loading.value = false
            }
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            val response = remoteRepository.searchNews(search = query)
            response.flow.cachedIn(viewModelScope).collect {
                _uiState.value = it
                _loading.value = false
            }
        }
    }

    //
    fun getNewsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            val response = remoteRepository.getNewsByCategory(category = category)
            response.flow.cachedIn(viewModelScope).collect {
                _uiState.value = it
                _loading.value = false
            }
        }
    }

    fun saveNews(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            offlineRepository.insertNews(article.toEntity())
        }
    }
}

fun Article.toEntity(): ArticleEntity = ArticleEntity(
    source = source.name,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content,
)

