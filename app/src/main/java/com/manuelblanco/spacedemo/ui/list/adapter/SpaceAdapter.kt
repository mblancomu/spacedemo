package com.manuelblanco.spacedemo.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manuelblanco.core.model.Products
import com.manuelblanco.spacedemo.R

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
class SpaceAdapter : RecyclerView.Adapter<SpaceViewHolder>() {

    var products: MutableList<Products> = mutableListOf()
    var listener: SpaceItemListeners? = null
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpaceViewHolder {
        return SpaceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SpaceViewHolder, position: Int) {
        listener?.let { listener -> holder.bind(products[position], listener) }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun addProducts(list: MutableList<Products>) {
        products.clear()
        products.addAll(list)
        notifyDataSetChanged()
    }
}