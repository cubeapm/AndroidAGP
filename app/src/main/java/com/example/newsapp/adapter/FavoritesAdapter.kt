package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemFavoriteNewsBinding
import com.example.newsapp.model.FavoriteNews

class FavoritesAdapter(
    private val onItemClick: (FavoriteNews) -> Unit,
    private val onFavoriteClick: (FavoriteNews) -> Unit
) : ListAdapter<FavoriteNews, FavoritesAdapter.FavoriteViewHolder>(FavoriteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding, onItemClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoriteViewHolder(
        private val binding: ItemFavoriteNewsBinding,
        private val onItemClick: (FavoriteNews) -> Unit,
        private val onFavoriteClick: (FavoriteNews) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: FavoriteNews) {
            binding.root.setOnClickListener { onItemClick(article) }
            binding.titleTextView.text = article.title
            binding.descriptionTextView.text = article.description
            binding.sourceTextView.text = article.sourceName
            binding.dateTextView.text = article.publishedAt
            binding.commentTextView.text = article.comment ?: "No comment"
            binding.favoriteButton.setOnClickListener { onFavoriteClick(article) }

            article.urlToImage?.let { imageUrl ->
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .into(binding.newsImageView)
            }
        }
    }

    class FavoriteDiffCallback : DiffUtil.ItemCallback<FavoriteNews>() {
        override fun areItemsTheSame(oldItem: FavoriteNews, newItem: FavoriteNews): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: FavoriteNews, newItem: FavoriteNews): Boolean {
            return oldItem == newItem
        }
    }
} 