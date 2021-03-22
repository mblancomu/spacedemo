package com.manuelblanco.spacedemo.ui.map

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.manuelblanco.spacedemo.R
import kotlinx.android.synthetic.main.item_product.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Created by Manuel Blanco Murillo on 3/22/21.
 */
class SpaceInfoWindow(val context: Context) : GoogleMap.InfoWindowAdapter {

    var markerCluster: MarkerClusterItem? = null
    val WIDTH = 800
    val HEIGHT = 1000

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }

    override fun getInfoContents(marker: Marker?): View {
        val infoView =
            (context as Activity).layoutInflater.inflate(R.layout.info_marker_product, null)

        infoView.tvTitle.text = markerCluster?.title
        infoView.tvDistance.text = markerCluster?.distance.toString().plus(" kms")
        infoView.tvPrice.text = markerCluster?.price.toString().plus(" ${markerCluster?.currency}")
        infoView.tvType.text = markerCluster?.type


        Glide.with(context)
            .load(markerCluster?.imageUrl)
            .centerInside()
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    infoView.ivThumbnail.setImageDrawable(resource)
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(100)
                        if (marker!!.isInfoWindowShown) {
                            marker.showInfoWindow()
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

        infoView.layoutParams =
            RelativeLayout.LayoutParams(WIDTH, HEIGHT)
        return infoView
    }

    fun setClusterItemInfo(markerClusterItem: MarkerClusterItem) {
        markerCluster = markerClusterItem
    }
}

