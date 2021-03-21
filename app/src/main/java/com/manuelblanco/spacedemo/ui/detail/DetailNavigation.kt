package com.manuelblanco.spacedemo.ui.detail

import androidx.navigation.NavController
import com.manuelblanco.spacedemo.R

/**
 * Created by Manuel Blanco Murillo on 3/21/21.
 */
object DetailNavigation {
    fun openDetail(navController: NavController) {
        navController.navigate(R.id.action_list_to_detail)
    }
}