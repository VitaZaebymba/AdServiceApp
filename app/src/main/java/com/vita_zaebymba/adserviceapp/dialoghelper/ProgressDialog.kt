package com.vita_zaebymba.adserviceapp.dialoghelper

import android.app.Activity
import android.app.AlertDialog
import com.vita_zaebymba.adserviceapp.databinding.ProgressDialogLayoutBinding
import com.vita_zaebymba.adserviceapp.databinding.SignDialogBinding

object ProgressDialog {
    fun createProgressDialog(act: Activity){

        val builder = AlertDialog.Builder(act)
        val rootDialogElement = ProgressDialogLayoutBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root // можно было сразу rootDialogElement передать в setView, но так более наглядно
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCancelable(false) // чтобы диалог нельзя было закрыть, пока выполянется задача

        dialog.show()
    }
}