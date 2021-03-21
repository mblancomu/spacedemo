package com.manuelblanco.spacedemo.ui.map

import androidx.appcompat.widget.Toolbar
import com.manuelblanco.spacedemo.ui.base.BaseFragment

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
class MapFragment: BaseFragment() {
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
        const val LAT_MADRID = 40.4215864
        const val LNG_MADRID = -3.6943134
    }

    override fun fetchData() {
        TODO("Not yet implemented")
    }

    override fun bindViewModel() {
        TODO("Not yet implemented")
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        TODO("Not yet implemented")
    }
}