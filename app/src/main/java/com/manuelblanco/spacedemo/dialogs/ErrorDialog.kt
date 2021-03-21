package com.manuelblanco.spacedemo.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.manuelblanco.spacedemo.R
import com.manuelblanco.spacedemo.databinding.DialogErrorBinding

class ErrorDialog(context: Context, private val message: String, private val listener: ErrorDialogListener) :
    Dialog(context, android.R.style.Theme_Material_Dialog) {

    lateinit var binding: DialogErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(context.resources?.getDrawable(android.R.color.transparent))

        binding = DialogErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews(binding, message)
    }

    private fun initViews(view: DialogErrorBinding, message: String) {
        view.dialogTitle.text = context.getString(R.string.title_error)
        view.dialogMessage.text = message
        view.buttonCancel.text = context.getString(R.string.btn_cancel)

        view.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

    interface ErrorDialogListener {
        fun onTrySelected()
    }
}