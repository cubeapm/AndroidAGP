package com.example.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.viewmodel.NewsViewModel
import com.newrelic.agent.android.NewRelic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter

    private companion object {
        private const val SIMULATE_ANR_ON_NEWS_CLICK = false
        private const val ANR_SLEEP_MS = 120_000L

        private const val SIMULATE_HTTP_ERROR_ON_NEWS_CLICK = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupSwipeRefresh()
        setupHeaderButtons()
        observeViewModel()
        loadNews()
        NewRelic.setInteractionName("Main Screen")
    }

    private fun setupHeaderButtons() {
        binding.favoriteButton.setOnClickListener {
            // TODO: Implement favorites navigation
            Toast.makeText(this, "Favorites feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.crashButton.setOnClickListener {
            simulateCrash()
        }
    }

    private fun simulateCrash() {
        try {
            val interactionId = NewRelic.startInteraction("CrashButtonClicked")
            Log.d("NewRelic", "Started Crash interaction with ID: $interactionId")
            // Simulate a crash by throwing an unhandled exception
            Thread.sleep(1000)
            NewRelic.endInteraction(interactionId)
            Log.d("NewRelic", "Ended Crash interaction with ID: $interactionId")
            throw RuntimeException("Simulated crash for testing purposes")
        } catch (e: Exception) {
            Log.e("NewRelic", "Error tracking Crash interaction", e)
            throw e
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter { article ->
            if (SIMULATE_ANR_ON_NEWS_CLICK) {
                try {
                    Thread.sleep(ANR_SLEEP_MS)
                } catch (_: InterruptedException) {
                    // ignore
                }
            }
            if (SIMULATE_HTTP_ERROR_ON_NEWS_CLICK) {
                fireRandomHttpError()
            }
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

    private fun fireRandomHttpError() {
        val test = mutableMapOf<String, Any>()
        test["percentage"] = 25
        NewRelic.recordCustomEvent("battery", "indmomey", test)
        val statusCode = (400..599).random()
        val url = "https://httpbin.org/status/$statusCode"
        Log.d("SimulateHTTP", "Firing request to $url")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                Log.d("SimulateHTTP", "Got ${response.code} from $url")
                response.close()
            } catch (e: Exception) {
                Log.e("SimulateHTTP", "Request to $url failed", e)
            }
        }
    }

    private fun loadNews() {
        try {
            val interactionId = NewRelic.startInteraction("News Load start")
            Log.d("NewRelic", "Started News Load interaction with ID: $interactionId")
            viewModel.loadNews()
            NewRelic.endInteraction(interactionId)
            Log.d("NewRelic", "Ended News Load interaction with ID: $interactionId")
        } catch (e: Exception) {
            Log.e("NewRelic", "Error tracking News Load interaction", e)
        }
    }
} 
