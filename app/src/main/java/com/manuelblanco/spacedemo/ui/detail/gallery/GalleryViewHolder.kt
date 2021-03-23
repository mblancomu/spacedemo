package com.manuelblanco.spacedemo.ui.detail.gallery

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manuelblanco.core.model.Products
import com.manuelblanco.spacedemo.R
import com.manuelblanco.spacedemo.databinding.ItemGalleryBinding
import com.manuelblanco.spacedemo.databinding.ItemProductBinding
import com.manuelblanco.spacedemo.ui.list.adapter.SpaceItemListeners

/**
 * Created by Manuel Blanco Murillo on 3/23/21.
 */
class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemGalleryBinding.bind(itemView)

    fun bind(imageUrl: String) {
        //Loading the image into the ImageView using Glide
        Glide
            .with(itemView.context)
            .load(imageUrl)
            .centerInside()
            .placeholder(R.drawable.placeholder_spacelens)
            .into(binding.ivGallery)
    }
}