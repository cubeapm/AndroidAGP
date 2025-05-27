package com.example.newsapp.api

import com.example.newsapp.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = "d8054e1997ed4572a601ce892e8b5d2b"
    ): NewsResponse
} 