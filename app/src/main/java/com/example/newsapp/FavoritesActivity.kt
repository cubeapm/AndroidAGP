package com.example.newsapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.FavoritesAdapter
import com.example.newsapp.databinding.ActivityFavoritesBinding
import com.example.newsapp.db.NewsDatabase
import com.newrelic.agent.android.NewRelic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: FavoritesAdapter
    private lateinit var database: NewsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite News"

        database = NewsDatabase(this)
        setupRecyclerView()
        loadFavorites()
        NewRelic.setInteractionName("Favorite Screen")
    }

    private fun setupRecyclerView() {
        adapter = FavoritesAdapter(
            onItemClick = { article ->
                // Open article in browser
                startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(article.url)))
            },
            onFavoriteClick = { article ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        database.deleteFavorite(article)
                        loadFavorites()
                    }
                }
            }
        )

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = this@FavoritesActivity.adapter
        }
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val favorites = database.getAllFavorites()
                withContext(Dispatchers.Main) {
                    adapter.submitList(favorites)
                    binding.emptyView.visibility = if (favorites.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
} 