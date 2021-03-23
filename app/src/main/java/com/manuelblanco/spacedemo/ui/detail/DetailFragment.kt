package com.manuelblanco.spacedemo.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.manuelblanco.core.model.Gallery
import com.manuelblanco.core.model.Products
import com.manuelblanco.spacedemo.R
import com.manuelblanco.spacedemo.common.viewLifecycle
import com.manuelblanco.spacedemo.databinding.FragmentDetailBinding
import com.manuelblanco.spacedemo.ext.bind
import com.manuelblanco.spacedemo.ui.base.BaseFragment
import com.manuelblanco.spacedemo.ui.detail.gallery.GalleryAdapter
import com.manuelblanco.spacedemo.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
@ExperimentalCoroutinesApi
class DetailFragment : BaseFragment() {

    val mainViewModel by viewModel<MainViewModel>()
    private var productDetail: Products? = null
    private var binding: FragmentDetailBinding by viewLifecycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val bottomBar =
            requireActivity().findViewById<View>(R.id.bottom_nav) as BottomNavigationView
        bottomBar.visibility = View.GONE
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar(binding.toolbar)
        fetchData()
        initAppBarLayout()
        fetchData()
        productDetail?.let { product -> initViewData(product) }
    }

    private fun initAppBarLayout() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            var isShow = false
            var scrollRange = -1

            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange;
            }
            if (scrollRange + verticalOffset == 0) {
                isShow = true;
            } else if (isShow) {
                isShow = false;
            }
        })

        binding.fab.setOnClickListener {
            //TODO("Missing this logic for implement with Room")
            Log.e("FAB", "Added to Favorites")
        }
    }

    override fun fetchData() {
        productDetail = mainViewModel.selectedProduct.value
    }

    override fun bindViewModel() {
        bind(mainViewModel.isLoading) {
            if (it) {
                showProgress(true, binding.appbar, binding.mainList, binding.fab)
            }
        }
        bind(mainViewModel.isFail) {
            if (it) {
                hideProgress(false, binding.appbar, binding.mainList, binding.fab)
                showErrorDialog(getString(R.string.msg_error))
            }
        }

        mainViewModel.selectedProduct.observe(viewLifecycleOwner, { product ->
            if (product != null) {
                hideProgress(false, binding.appbar, binding.mainList, binding.fab)
            }
        })
    }

    private fun initViewData(product: Products) {
        binding.collapsingToolbar.title = product.title
        binding.textDescription.text = product.description
        binding.textAddress.text = product.address
        binding.textLikes.text = product.likes.toString()
        binding.textRating.text = product.rating_amount.toString()
        binding.textOwner.text = product.owner
        binding.textCategory.text = product.category
        binding.textPrice.text = product.price.toString().plus(" ${product.currency}")

        context?.let {
            Glide
                .with(it)
                .load(product.gallery[0].src)
                .centerCrop()
                .placeholder(R.drawable.placeholder_spacelens)
                .into(binding.detailImg)
        }

        if (!product.gallery.isNullOrEmpty()){
            initGallery(product)
        } else {
            binding.cardGallery.visibility = View.GONE
        }
    }

    private fun initGallery(product: Products){
        val imagesUrl = mutableListOf<String>()
        product.gallery.forEach {
            imagesUrl.add(it.src)
        }
        val galleryAdapter = GalleryAdapter()
        binding.gallery.apply {
            adapter = galleryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        galleryAdapter.addImages(imagesUrl)
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun showProgress(
        isVisible: Boolean, appBar: AppBarLayout, mainList: NestedScrollView,
        fab: FloatingActionButton,
    ) {
        val view = if (isVisible) View.INVISIBLE else View.VISIBLE
        appBar.visibility = view
        mainList.visibility = view
        fab.visibility = view
    }

    private fun hideProgress(
        isVisible: Boolean, appBar: AppBarLayout, mainList: NestedScrollView,
        fab: FloatingActionButton,
    ) {
        val view = if (isVisible) View.INVISIBLE else View.VISIBLE
        appBar.visibility = view
        mainList.visibility = view
        fab.visibility = view

    }
}