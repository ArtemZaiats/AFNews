package com.example.afnews.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afnews.data.Article
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

    init {
        getAllNews()
    }

    fun getAllNews() {
        viewModelScope.launch(Dispatchers.IO) {
            remoteRepository.getAllNewsStream().map {
                when (it) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "response: ${it.data}")
                        _uiState.value = HomeUiState(it.data?.articles!!)
                    }

                    is NetworkResult.Error -> {
                        Log.e(TAG, "Unsuccessful response: ${it.message}")
                    }

                    is NetworkResult.Loading -> {
                        Log.d(TAG, "Loading...")
                    }
                }
            }.stateIn(scope = viewModelScope)
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            remoteRepository.searchNews(query).map {
                when (it) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "response: ${it.data}")
                        _uiState.value = HomeUiState(it.data?.articles!!)
                    }

                    is NetworkResult.Error -> {
                        Log.e(TAG, "Unsuccessful response: ${it.message}")
                    }

                    is NetworkResult.Loading -> {
                        Log.d(TAG, "Loading...")
                    }
                }
            }.stateIn(scope = viewModelScope)
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}

data class HomeUiState(val newsList: List<Article> = listOf())

