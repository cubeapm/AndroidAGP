package com.example.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsArticle
import com.example.newsapp.repository.NewsRepository
import com.newrelic.agent.android.NewRelic
import kotlinx.coroutines.launch
import java.util.UUID



class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    
    private val _newsArticles = MutableLiveData<List<NewsArticle>>()
    val newsArticles: LiveData<List<NewsArticle>> = _newsArticles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadNews() {

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getTopHeadlines()
                _newsArticles.value = response.articles
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
        NewRelic.endInteraction("my_interaction")
    }
} 