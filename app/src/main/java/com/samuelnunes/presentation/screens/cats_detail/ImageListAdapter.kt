package com.samuelnunes.presentation.screens.cats_detail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.utility_tool_kit.extensions.inflater
import com.samuelnunes.utils.databinding.ImageItemBinding

class ImageListAdapter: ListAdapter<Breed.Image, ImageListAdapter.ImageViewHolder>(ImageCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListAdapter.ImageViewHolder =
        ImageViewHolder(ImageItemBinding.inflate(parent.inflater, parent, false))

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ImageViewHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Breed.Image) {
            binding.ivCat.load(image.url)
        }
    }

    internal object ImageCallback : DiffUtil.ItemCallback<Breed.Image>() {
        override fun areItemsTheSame(first: Breed.Image, second: Breed.Image): Boolean =
            first.id == second.id

        override fun areContentsTheSame(first: Breed.Image, second: Breed.Image): Boolean =
            first == second
    }

}