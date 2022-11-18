package com.vita_zaebymba.adserviceapp.accounthelper

import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.vita_zaebymba.adserviceapp.MainActivity
import com.vita_zaebymba.adserviceapp.R

class AccountHelper(act:MainActivity) {

    private  val act = act // передаем активити

    fun signUpWithEmail(email:String, password: String){
        if (email.isNullOrEmpty() && password.isNullOrEmpty()){
            act.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { // функция addOnComplete возвращает task - специальный объект,
                // который несет информацию об успешности регистрации
                    task -> if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!)

            } else {
                Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                }

             }
        }
    }
    private  fun sendEmailVerification(user:FirebaseUser){ // функция для отправки письмо с подтверждением
            user.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act, act.resources.getString(R.string.send_verification_done), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(act, act.resources.getString(R.string.send_verification_email_error), Toast.LENGTH_LONG).show()
                }
            }
    }
}