package com.vita_zaebymba.adserviceapp.dialoghelper

import android.app.AlertDialog
import com.vita_zaebymba.adserviceapp.MainActivity
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.databinding.SignDialogBinding

class DialogHelper(act:MainActivity) {
    private val act = act
    fun createSignDialog(index:Int){
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root // можно было сразу rootDialogElement передать в setView, но так более наглядно

        if (index == DialogConst.SIGN_UP_STATE){ // меняем диалоговое окно в зависимости от вохода или регистрации
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ad_sign_up)
            rootDialogElement.btSignUp.text = act.resources.getString(R.string.sign_up_action)

        } else {
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ad_sign_in)
            rootDialogElement.btSignUp.text = act.resources.getString(R.string.sign_in_action)
        }

        builder.setView(view)
        builder.show()
    }
}