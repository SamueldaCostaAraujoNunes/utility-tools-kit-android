package com.samuelnunes.presentation.screens.cats_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.utils.databinding.ItemGifBinding

internal class CatGifListAdapter(private val onImageClick: (Breed.Image) -> Unit) :
    ListAdapter<Breed.Image, CatGifListAdapter.ImageViewHolder>(ImageItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGifBinding.inflate(layoutInflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ImageViewHolder(private val binding: ItemGifBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Breed.Image) {
            binding.apply {
                catUrl = item.url
                root.setOnClickListener { onImageClick(item) }
            }
        }
    }

    internal object ImageItemCallback : DiffUtil.ItemCallback<Breed.Image>() {
        override fun areItemsTheSame(first: Breed.Image, second: Breed.Image): Boolean =
            first.id == second.id

        override fun areContentsTheSame(first: Breed.Image, second: Breed.Image): Boolean =
            first == second

    }

}