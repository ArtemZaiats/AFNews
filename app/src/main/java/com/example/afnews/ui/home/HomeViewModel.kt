package com.example.afnews.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afnews.data.Article
import com.example.afnews.data.ArticleEntity
import com.example.afnews.data.network.NetworkResult
import com.example.afnews.data.repository.NewsOfflineRepository
import com.example.afnews.data.repository.NewsRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: NewsRemoteRepository,
    private val offlineRepository: NewsOfflineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(emptyList()))
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        getAllNews()
    }

    fun getAllNews(sortBy: String = "publishedAt") {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            remoteRepository.getAllNewsStream(sortBy = sortBy).map {
                when (it) {
                    is NetworkResult.Success -> {
                        _loading.value = false
                        Log.d(TAG, "response: ${it.data}")
                        _uiState.value = HomeUiState(it.data?.articles!!)
                    }

                    is NetworkResult.Error -> {
                        Log.e(TAG, "Unsuccessful response: ${it.message}")
                        _loading.value = false
                    }

                    is NetworkResult.Loading -> {
                        Log.d(TAG, "Loading...")
                        _loading.value = true
                    }
                }
            }.stateIn(scope = viewModelScope)
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            remoteRepository.searchNews(query).map {
                when (it) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "response: ${it.data}")
                        _uiState.value = HomeUiState(it.data?.articles!!)
                        _loading.value = false
                    }

                    is NetworkResult.Error -> {
                        Log.e(TAG, "Unsuccessful response: ${it.message}")
                        _loading.value = false
                    }

                    is NetworkResult.Loading -> {
                        Log.d(TAG, "Loading...")
                        _loading.value = true
                    }
                }
            }.stateIn(scope = viewModelScope)
        }
    }

    fun getNewsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            remoteRepository.getNewsByCategory(category = category).map {
                when (it) {
                    is NetworkResult.Success -> {
                        _loading.value = false
                        Log.d(TAG, "response: ${it.data}")
                        _uiState.value = HomeUiState(it.data?.articles!!)
                    }

                    is NetworkResult.Error -> {
                        Log.e(TAG, "Unsuccessful response: ${it.message}")
                        _loading.value = false
                    }

                    is NetworkResult.Loading -> {
                        Log.d(TAG, "Loading...")
                        _loading.value = true
                    }
                }
            }.stateIn(scope = viewModelScope)
        }
    }

    fun saveNews(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            offlineRepository.insertNews(article.toEntity())
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}

data class HomeUiState(val newsList: List<Article> = listOf())

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

