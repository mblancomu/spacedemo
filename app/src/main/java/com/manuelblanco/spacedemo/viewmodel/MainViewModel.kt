package com.manuelblanco.spacedemo.viewmodel

import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.manuelblanco.core.model.Products
import com.manuelblanco.core.model.Resource
import com.manuelblanco.core.repository.SpaceRepository
import com.manuelblanco.spacedemo.ui.map.MapFragment.Companion.LAT_SHAIGON
import com.manuelblanco.spacedemo.ui.map.MapFragment.Companion.LNG_SHAIGON
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
@ExperimentalCoroutinesApi
class MainViewModel(
    private val repository: SpaceRepository
) : ViewModel() {
    private val _userLocation = MutableLiveData<Location>()
    private var _page = MutableLiveData<Int>()
    private val productsEvent = MutableSharedFlow<Unit>()
    private var _products = MutableLiveData<List<Products>>()
    private var _selectedProduct = MutableLiveData<Products>()
    var isNetworkAvailable = MutableLiveData<Boolean>()

    val userLocation = _userLocation
    val selectedProduct = _selectedProduct
    val page = _page
    val products = _products

    init {
        if (_userLocation.value == null) {
            _userLocation.value = fakeLocation()
        }

        if (_page.value == null) {
            _page.value = 1
        }

//        viewModelScope.launch {
//            productsEvent.emit(Unit)
//        }
    }

    fun setUserLocation(location: Location) {
        if (_userLocation.value != location) {
            _userLocation.value = location
        }
    }

    fun setSelectedProduct(product: Products) {
        if (_selectedProduct.value != product) {
            _selectedProduct.value = product
        }
    }

    fun updatePage(page: Int) {
        if (_page.value != page) {
            _page.value = page
        }
    }

    private val productFlow = productsEvent.flatMapLatest {
        repository.getProducts(
            _userLocation.value?.latitude.toString(),
            _userLocation.value?.longitude.toString(), _page.value!!
        )
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading)

    val isLoading = productFlow.map { it.isLoading }
    val isFail = productFlow.map { it.isFail }
    val data =
        productFlow.map { it.valueOrNull?.products }.asLiveData(viewModelScope.coroutineContext)

    fun fakeLocation(): Location {
        val location = Location(LocationManager.GPS_PROVIDER)
        location.latitude = LAT_SHAIGON
        location.longitude = LNG_SHAIGON
        return location
    }

    fun fetchProducts() {
        viewModelScope.launch {
            productsEvent.emit(Unit)
        }
    }
}