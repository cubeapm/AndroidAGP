package com.example.newsapp.model

data class FavoriteNews(
    val url: String,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val sourceName: String,
    val publishedAt: String,
    val comment: String? = null,
    val timestamp: Long = System.currentTimeMillis()
) 