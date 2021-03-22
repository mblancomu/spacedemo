package com.manuelblanco.spacedemo.ui.map

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.manuelblanco.core.model.Products
import com.manuelblanco.spacedemo.R
import com.manuelblanco.spacedemo.common.viewLifecycle
import com.manuelblanco.spacedemo.databinding.FragmentMapBinding
import com.manuelblanco.spacedemo.ext.bind
import com.manuelblanco.spacedemo.ui.base.BaseFragment
import com.manuelblanco.spacedemo.ui.detail.DetailNavigation
import com.manuelblanco.spacedemo.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
@ExperimentalCoroutinesApi
class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var binding: FragmentMapBinding by viewLifecycle()
    private val mainViewModel by viewModel<MainViewModel>()
    lateinit var clusterManager: ClusterManager<MarkerClusterItem>
    private var listOfProducts: List<Products>? = ArrayList()
    private var zoomByDefault = 8f
    var counterZoom = 0
    private val WIDTH_CLUSTER = 250
    private val HEIGHT_CLUSTER = 250

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1

        const val LAT_SHAIGON = 10.799467
        const val LNG_SHAIGON = 106.709507
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        setUpToolbar(binding.toolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setUpMap() {
        if (context?.let { context ->
                ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { activity ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            return
        }
        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    private fun addProductsOnMap(listProducts: List<Products>) {
        listProducts.forEach { product ->
            val markerPosition = LatLng(product.location.latitude, product.location.longitude)
            val clusterItem = MarkerClusterItem(
                product.title,
                markerPosition,
                product.product_id.toString(),
                product.gallery[0].src,
                product.distance,
                product.currency,
                product.category,
                product.price
            )
            clusterManager.addItem(clusterItem)
        }
        clusterManager.cluster()
    }

    override fun fetchData() {
        mainViewModel.fetchProducts()
    }

    override fun bindViewModel() {
        bind(mainViewModel.isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        }

        bind(mainViewModel.isFail) {
            if (it) {
                binding.progressBar.visibility = View.GONE
                showErrorDialog(getString(R.string.msg_error))
            }
        }

        mainViewModel.data.observe(viewLifecycleOwner, { products ->
            if (!products.isNullOrEmpty()) {
                listOfProducts = products
                setupClusterManager()
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            title = getString(R.string.app_name)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.logo_toolbar)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            this.map = map
        }

        clusterManager = ClusterManager(context, map)
        map?.uiSettings?.isZoomControlsEnabled = true
        val madridLatLng = LatLng(LAT_SHAIGON, LNG_SHAIGON)

        val infoWindow = context?.let { context -> SpaceInfoWindow(context) }
        map?.setInfoWindowAdapter(clusterManager.markerManager)
        map?.setOnInfoWindowClickListener(clusterManager)
        clusterManager.markerCollection.setInfoWindowAdapter(infoWindow)

        clusterManager.setOnClusterItemInfoWindowClickListener { markerCluster ->
            val product = listOfProducts?.find { it.product_id == markerCluster.snippet ?: 0 }
            product?.let { item ->
                mainViewModel.setSelectedProduct(item)
                DetailNavigation.openDetail(findNavController())
            }
        }

        clusterManager.setOnClusterClickListener {
            val builder = LatLngBounds.builder()
            val productMarkers = it.items

            for (item in productMarkers) {
                val productPosition = item.position
                builder.include(productPosition)
            }

            val bounds = builder.build()

            try {
                map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            } catch (error: Exception) {
                Log.e("ERROR", error.message.toString())
            }

            true
        }

        clusterManager.setOnClusterItemClickListener { item ->
            counterZoom = 0
            infoWindow?.setClusterItemInfo(item)
            false
        }

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(madridLatLng, zoomByDefault))
        map?.setOnCameraIdleListener(clusterManager)
        map?.setOnMarkerClickListener(clusterManager)
        map?.setOnInfoWindowClickListener(clusterManager)
        setUpMap()
    }

    private fun setRenderer() {
        CoroutineScope(Dispatchers.Main).launch {
            clusterManager.renderer = CustomRenderer()

        }
    }

    private inner class CustomRenderer() :
        DefaultClusterRenderer<MarkerClusterItem>(context, map, clusterManager) {
        override fun onBeforeClusterItemRendered(
            item: MarkerClusterItem,
            markerOptions: MarkerOptions
        ) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_space))
        }

        override fun onClusterItemRendered(item: MarkerClusterItem, marker: Marker) {

            Glide.with(context!!)
                .load(item.imageUrl)
                .centerInside()
                .override(WIDTH_CLUSTER, HEIGHT_CLUSTER)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val icon = drawableToBitmap(resource)
                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        override fun shouldRenderAsCluster(cluster: Cluster<MarkerClusterItem>): Boolean {
            return cluster.size > 10
        }
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    private fun setupClusterManager() {
        map.clear()
        clusterManager.clearItems()
        CoroutineScope(Dispatchers.Main).launch {
            addClusterItems()
            setRenderer()
        }
    }

    private fun addClusterItems() {
        listOfProducts?.let { products ->
            addProductsOnMap(products)
        }
    }
}