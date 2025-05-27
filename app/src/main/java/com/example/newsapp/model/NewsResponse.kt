package com.example.newsapp.model

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
)

data class Source(
    val id: String?,
    val name: String
) 