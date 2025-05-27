package com.example.newsapp.repository

import com.example.newsapp.api.NewsApiService
import com.example.newsapp.model.NewsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class NewsRepository {
    private val apiService: NewsApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(NewsApiService::class.java)
    }

    suspend fun getTopHeadlines(): NewsResponse {
        return apiService.getTopHeadlines()
    }
} 