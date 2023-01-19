package com.vita_zaebymba.adserviceapp.dialoghelper

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.vita_zaebymba.adserviceapp.MainActivity
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.accounthelper.AccountHelper
import com.vita_zaebymba.adserviceapp.databinding.SignDialogBinding

class DialogHelper(act:MainActivity) {
    private val act = act
    val accHelper = AccountHelper(act)

    fun createSignDialog(index:Int){
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = SignDialogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root // можно было сразу rootDialogElement передать в setView, но так более наглядно
        builder.setView(view)

        setDialogState(index, rootDialogElement)

        val dialog = builder.create()

        rootDialogElement.btSignUpIn.setOnClickListener{
            setOnClickSignUpIn(index, rootDialogElement, dialog)
        }
        rootDialogElement.btForgetPassword.setOnClickListener{
            setOnClickResetPassword(rootDialogElement, dialog)
        }

        rootDialogElement.btGoogleSignIn.setOnClickListener{
            accHelper.signInWithGoogle()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setOnClickResetPassword(rootDialogElement: SignDialogBinding, dialog: AlertDialog?) { // функция восстановления пароля


        if (rootDialogElement.edSignEmail.text.isNotEmpty()){
            act.mAuth.sendPasswordResetEmail(rootDialogElement.edSignEmail.text.toString()).addOnCompleteListener { // отправка письма с ссылкой на восстановление
                task ->
                if (task.isSuccessful){
                    Toast.makeText(act, R.string.email_reset_password_was_sent, Toast.LENGTH_LONG).show()
                }
            }
            dialog?.dismiss() // спрятать диалоговое окно
        } else{
            rootDialogElement.tvDialogMessage.visibility = View.VISIBLE
        }

    }

    private fun setOnClickSignUpIn(index: Int, rootDialogElement: SignDialogBinding, dialog: AlertDialog?) {
        dialog?.dismiss() // закрытие окна с регистрацией после нажатия на кнопку
        if (index == DialogConst.SIGN_UP_STATE){

            accHelper.signUpWithEmail(rootDialogElement.edSignEmail.text.toString(), rootDialogElement.edSignPassword.text.toString())

        } else {
            accHelper.signInpWithEmail(rootDialogElement.edSignEmail.text.toString(), rootDialogElement.edSignPassword.text.toString())

        }

    }

    private fun setDialogState(index: Int, rootDialogElement: SignDialogBinding) {
        if (index == DialogConst.SIGN_UP_STATE){ // меняем диалоговое окно в зависимости от вохода или регистрации
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ad_sign_up)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_up_action)

        } else {
            rootDialogElement.tvSignTitle.text = act.resources.getString(R.string.ad_sign_in)
            rootDialogElement.btSignUpIn.text = act.resources.getString(R.string.sign_in_action)
            rootDialogElement.btForgetPassword.visibility = View.VISIBLE
        }

    }
}