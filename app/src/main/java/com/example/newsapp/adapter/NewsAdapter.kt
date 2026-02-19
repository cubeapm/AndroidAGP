package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemNewsBinding
import com.example.newsapp.model.NewsArticle

class NewsAdapter(
    private val onItemClick: (NewsArticle) -> Unit
) : ListAdapter<NewsArticle, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsViewHolder(
        private val binding: ItemNewsBinding,
        private val onItemClick: (NewsArticle) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: NewsArticle) {
            binding.root.setOnClickListener {
                onItemClick(article)
            }
            binding.titleTextView.text = article.title
            binding.descriptionTextView.text = article.description
            binding.sourceTextView.text = article.source.name
            binding.dateTextView.text = article.publishedAt

            article.urlToImage?.let { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .into(binding.newsImageView)
            }
        }
    }

    private class NewsDiffCallback : DiffUtil.ItemCallback<NewsArticle>() {
        override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean {
            return oldItem == newItem
        }
    }
} 