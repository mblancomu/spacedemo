package com.manuelblanco.spacedemo.ui.list.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manuelblanco.core.model.Products
import com.manuelblanco.spacedemo.R
import com.manuelblanco.spacedemo.databinding.ItemProductBinding

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
class SpaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemProductBinding.bind(itemView)

    fun bind(product: Products, listener: SpaceItemListeners) {
        initListeners(listener, product)
        val imageUrl = product.gallery[0].src
        binding.tvTitle.text = product.title
        binding.tvPrice.text = product.price.toString().plus(" $")
        binding.tvType.text = product.category
        binding.tvDistance.text = product.distance.toString().plus(" kms")
        //Loading the image into the ImageView using Glide
        Glide
            .with(itemView.context)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_spacelens)
            .into(binding.ivThumbnail)
    }

    private fun initListeners(listener: SpaceItemListeners, product: Products) {
        binding.root.setOnClickListener {
            listener.onItemClickListener(product)
        }
    }
}