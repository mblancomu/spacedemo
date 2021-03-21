package com.manuelblanco.spacedemo.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manuelblanco.core.model.Products
import com.manuelblanco.spacedemo.R
import com.manuelblanco.spacedemo.common.viewLifecycle
import com.manuelblanco.spacedemo.databinding.FragmentListBinding
import com.manuelblanco.spacedemo.ext.bind
import com.manuelblanco.spacedemo.ui.base.BaseFragment
import com.manuelblanco.spacedemo.ui.detail.DetailNavigation
import com.manuelblanco.spacedemo.ui.list.adapter.SpaceAdapter
import com.manuelblanco.spacedemo.ui.list.adapter.SpaceItemListeners
import com.manuelblanco.spacedemo.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
@ExperimentalCoroutinesApi
class ListFragment : BaseFragment(), SpaceItemListeners {

    lateinit var gridLayoutManager: GridLayoutManager
    private val mainViewModel by viewModel<MainViewModel>()
    private var binding: FragmentListBinding by viewLifecycle()
    lateinit var productsAdapter: SpaceAdapter
    private var listOfProducts: MutableList<Products> = mutableListOf()
    private var isLoading: Boolean = false
    private var isFromUpdate: Boolean = false
    private var isLastPage = false
    private var PAGE_SIZE = 19
    private var initItem = 0
    private var lastItem = PAGE_SIZE
    private var PAGE = 1
    private var tempListPage: MutableList<Products> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bottomBar =
            requireActivity().findViewById<View>(R.id.bottom_nav) as BottomNavigationView
        bottomBar.visibility = View.VISIBLE
        binding = FragmentListBinding.inflate(inflater, container, false)
        setUpToolbar(binding.toolbar)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridLayoutManager = GridLayoutManager(context, 2)

        initSwipeRefresh()
        setUpAdapter()
        onRefresh()
        paginationScroll()
    }

    override fun onResume() {
        super.onResume()
        bindViewModel()
    }

    private fun setUpAdapter() {
        binding.emptyList.text =
            getString(R.string.empty_list)
        productsAdapter = SpaceAdapter()
        productsAdapter.listener = this

        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = productsAdapter
        }
    }

    private fun onRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            fetchData()
        }
    }

    /**
     * Custom toolbar for fragments.
     */
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

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun dismissSwipeRefresh() {
        binding.swipeRefresh.isRefreshing = false
    }

    private fun showProgress(isFromUpdate: Boolean) {
        dismissSwipeRefresh()
        if (isFromUpdate) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.emptyList.visibility = View.GONE
            binding.swipeRefresh.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgress() {
        dismissSwipeRefresh()
        binding.emptyList.visibility = View.GONE
        binding.swipeRefresh.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun emptyView() {
        dismissSwipeRefresh()
        binding.emptyList.visibility = View.VISIBLE
        binding.swipeRefresh.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    override fun fetchData() {
        mainViewModel.fetchProducts()
        dismissSwipeRefresh()
    }

    override fun bindViewModel() {
        bind(mainViewModel.isLoading) {
            if (it) showProgress(isFromUpdate) else hideProgress()
        }
        bind(mainViewModel.isFail) {
            if (it) {
                hideProgress()
                emptyView()
                showErrorDialog(getString(R.string.msg_error))
            }
        }

        mainViewModel.data.observe(viewLifecycleOwner, { products ->
            if (!products.isNullOrEmpty()) {
                if (isFromUpdate) {
                    listOfProducts.addAll(products)
                    isFromUpdate = false
                    isLoading = false
                    updateList(getNextPage(initItem,
                        lastItem))
                } else {
                    listOfProducts = products as MutableList<Products>

                    productsAdapter.addProducts(
                        if (listOfProducts.isNullOrEmpty()) mutableListOf() else getNextPage(
                            initItem,
                            lastItem
                        )
                    )
                }
                hideProgress()
            }

            isLoading = false
        })
    }

    override fun onItemClickListener(product: Products) {
        mainViewModel.setSelectedProduct(product)
        DetailNavigation.openDetail(findNavController())
    }

    private fun getNextPage(start: Int, end: Int): MutableList<Products> {
        if (start < listOfProducts.size && end < listOfProducts.size) {
            tempListPage.addAll(listOfProducts.subList(start, end+1))
            return tempListPage
        } else if (start < listOfProducts.size) {
            tempListPage.addAll(listOfProducts.subList(start, listOfProducts.size))
            return tempListPage
        }
        return mutableListOf()
    }

    private fun updateList(products: MutableList<Products>) {
        if (!products.isNullOrEmpty()){
            productsAdapter.addProducts(products)
        } else {
            isLastPage = true
        }
        hideProgress()
        isLoading = false
    }

    /**
     * Scroll Listener for downloading data in the list. They will be downloaded in batches of 10 items.
     */
    private fun paginationScroll() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == lastItem) {
                        initItem = lastItem + 1
                        lastItem = initItem + PAGE_SIZE

                        if (lastItem < listOfProducts.size) {
                            isLoading = true
                            showProgress(true)
                            updateList(getNextPage(initItem, lastItem))
                        } else {
                            if (!isLastPage) {
                                PAGE += 1
                                mainViewModel.updatePage(PAGE)
                                isFromUpdate = true
                                fetchData()
                            }
                        }
                    }
                }
            }
        })
    }
}