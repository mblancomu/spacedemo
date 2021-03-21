package com.manuelblanco.spacedemo.ui.base

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.manuelblanco.spacedemo.dialogs.ErrorDialog

/**
 * Created by Manuel Blanco Murillo on 3/20/21.
 */
abstract class BaseFragment : Fragment() {

    abstract fun fetchData()

    abstract fun bindViewModel()

    abstract fun setUpToolbar(toolbar: Toolbar)

    fun showErrorDialog(message: String) {
        val dialogError = context?.let {
            ErrorDialog(it, message, object : ErrorDialog.ErrorDialogListener {
                override fun onTrySelected() {
                }
            })
        }
        dialogError?.setCanceledOnTouchOutside(false)
        if (dialogError != null && !dialogError.isShowing) {
            dialogError.show()
        }
    }
}