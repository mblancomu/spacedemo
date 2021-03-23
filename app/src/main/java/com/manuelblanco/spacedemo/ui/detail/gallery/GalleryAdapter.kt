package com.manuelblanco.spacedemo.ui.detail.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manuelblanco.spacedemo.R
import com.manuelblanco.spacedemo.ui.list.adapter.SpaceItemListeners

/**
 * Created by Manuel Blanco Murillo on 3/23/21.
 */
class GalleryAdapter : RecyclerView.Adapter<GalleryViewHolder>() {

    var imagesUrl: MutableList<String> = mutableListOf()
    var listener: SpaceItemListeners? = null
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(imagesUrl[position])
    }

    override fun getItemCount(): Int {
        return imagesUrl.size
    }

    fun addImages(list: MutableList<String>) {
        imagesUrl.clear()
        imagesUrl.addAll(list)
        notifyDataSetChanged()
    }
}