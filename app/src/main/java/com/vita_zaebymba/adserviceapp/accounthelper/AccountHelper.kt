package com.vita_zaebymba.adserviceapp.accounthelper

import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import com.vita_zaebymba.adserviceapp.MainActivity
import com.vita_zaebymba.adserviceapp.R

class AccountHelper(act:MainActivity) {

    private  val act = act // передаем активити
    private lateinit var signInClient: GoogleSignInClient

    fun signUpWithEmail(email:String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty()){
            act.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { // функция addOnComplete возвращает task - специальный объект,который несет информацию об успешности регистраци

                    task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!) //отправляем email
                        act.uiUpdate(task.result?.user)

                    }
                    else {
                        Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                }

             }
        }
    }

    fun signInpWithEmail(email:String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty()){
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { // функция addOnComplete возвращает task - специальный объект,который несет информацию об успешности регистраци

                    task ->
                if (task.isSuccessful) {
                    act.uiUpdate(task.result?.user)
                }
                else {
                    Toast.makeText(act, act.resources.getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
                }

            }
        }
    }


    private fun sendEmailVerification(user:FirebaseUser){ // функция для отправки письма с подтверждением
            user.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act, act.resources.getString(R.string.send_verification_done), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(act, act.resources.getString(R.string.send_verification_email_error), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun getSignInClient():GoogleSignInClient { // класс GoogleSignInClient создает интент для отправки сообщения к системе для получения доступа к аккаунту
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(act.getString(R.string.default_web_client_id)).build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun signInWithGoogle(){
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
    }


}