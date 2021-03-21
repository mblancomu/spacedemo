package com.manuelblanco.spacedemo.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.manuelblanco.spacedemo.databinding.DialogInfoBinding

class InfoDialog(
    context: Context, private val message: String, private val title: String,
    private val listener: InfoDialogListener
) :
    Dialog(context, android.R.style.Theme_Material_Dialog) {

    lateinit var binding: DialogInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(context.resources?.getDrawable(android.R.color.transparent))

        binding = DialogInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews(binding)
    }

    private fun initViews(view: DialogInfoBinding) {
        binding.dialogTitle.text = title
        binding.dialogMessage.text = message
        view.buttonCancel.setOnClickListener {
            dismiss()
        }

        view.buttonOk.setOnClickListener {
            listener.onOKSelected()
            dismiss()
        }
    }

    interface InfoDialogListener {
        fun onOKSelected()
    }
}