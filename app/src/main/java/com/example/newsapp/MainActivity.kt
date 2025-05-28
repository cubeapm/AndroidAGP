package com.example.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.db.NewsDatabase
import com.example.newsapp.model.FavoriteNews
import com.example.newsapp.viewmodel.NewsViewModel
import com.newrelic.agent.android.NewRelic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    private lateinit var database: NewsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = NewsDatabase(this)
        setupViewModel()
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        loadNews()
        NewRelic.setInteractionName("Main Screen")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorites -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter(
            onItemClick = { article ->
                // Open article in browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                startActivity(intent)
            },
            onFavoriteClick = { article ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val existingFavorite = database.getFavoriteByUrl(article.url)
                        if (existingFavorite != null) {
                            database.deleteFavorite(existingFavorite)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity, "Removed from favorites", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val favoriteNews = FavoriteNews(
                                url = article.url,
                                title = article.title,
                                description = article.description,
                                urlToImage = article.urlToImage,
                                sourceName = article.source.name,
                                publishedAt = article.publishedAt
                            )
                            database.insertFavorite(favoriteNews)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity, "Added to favorites", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        )
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