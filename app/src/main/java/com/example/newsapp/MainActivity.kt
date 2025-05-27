package com.example.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.viewmodel.NewsViewModel
import com.newrelic.agent.android.NewRelic

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        loadNews()

        NewRelic.withApplicationToken(
            "AA9a15089936d85357e12515354e7ff46e965cf9c6-NRMA"
        ).start(this.applicationContext)

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter { article ->
            // Open article in browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(intent)
        }
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadNews()
        }
    }

    private fun observeViewModel() {
        viewModel.newsArticles.observe(this) { articles ->
            adapter.submitList(articles)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadNews() {
        viewModel.loadNews()
    }
} 