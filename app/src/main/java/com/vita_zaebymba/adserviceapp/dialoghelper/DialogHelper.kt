package com.vita_zaebymba.adserviceapp.dialoghelper

import android.app.AlertDialog
import com.vita_zaebymba.adserviceapp.MainActivity
import com.vita_zaebymba.adserviceapp.databinding.SignDialogBinding

class DialogHelper(act:MainActivity) {
    private val act = act
    fun createSignDialog(){
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root // можно было сразу rootDialogElement передать в setView, но так более наглядно
        builder.setView(view)
        builder.show()
    }
}