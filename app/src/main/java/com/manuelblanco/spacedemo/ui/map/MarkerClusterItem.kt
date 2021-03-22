package com.manuelblanco.spacedemo.ui.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * Created by Manuel Blanco Murillo on 3/22/21.
 */
class MarkerClusterItem(
    private val title: String,
    private val position: LatLng,
    private val snippet: String,
    var imageUrl: String,
    var distance: Int,
    var currency: String,
    var type: String,
    var price: Int
) : ClusterItem {
    override fun getSnippet(): String? {
        return snippet
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getPosition(): LatLng {
        return position
    }
}