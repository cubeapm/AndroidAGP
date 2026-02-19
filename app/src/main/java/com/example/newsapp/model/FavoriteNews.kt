package com.example.newsapp.model

data class FavoriteNews(
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val sourceName: String,
    val publishedAt: String,
    val comment: String? = null
) 