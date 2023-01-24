package com.vita_zaebymba.adserviceapp.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.vita_zaebymba.adserviceapp.R

class DialogSpinnerHelper {
    fun showSpinnerDialog(context: Context, list: ArrayList<String>){
        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null) // Превращаем разметку в код
        builder.setView(rootView)
        builder.show()
    }
}