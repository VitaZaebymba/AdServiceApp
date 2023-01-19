package com.vita_zaebymba.adserviceapp.accounthelper

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.vita_zaebymba.adserviceapp.MainActivity
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.constants.FirebaseAuthConstants
import com.vita_zaebymba.adserviceapp.dialoghelper.GoogleAccConst

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
                        //Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                        //Log.d("MyLog", "Exception: ${exception.errorCode}")
                        if (task.exception is FirebaseAuthUserCollisionException){
                            val exception = task.exception as FirebaseAuthUserCollisionException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE){
                                //Toast.makeText(act, FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE, Toast.LENGTH_LONG).show()
                                linkEmailToGmail(email, password)
                            }
                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            val exception = task.exception as FirebaseAuthInvalidCredentialsException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL){
                                Toast.makeText(act, FirebaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                                //Log.d("MyLog", "Exception: ${exception.errorCode}")
                            }
                        }
                        if (task.exception is FirebaseAuthWeakPasswordException) {
                            val exception = task.exception as FirebaseAuthWeakPasswordException
                            Log.d("MyLog", "Exception: ${exception.errorCode}")
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_WEAK_PASSWORD) {
                                Toast.makeText(act, FirebaseAuthConstants.ERROR_WEAK_PASSWORD, Toast.LENGTH_LONG).show()
                                //Log.d("MyLog", "Exception: ${exception.errorCode}")
                            }
                        }
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
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            Log.d("MyLog", "Exception: ${task.exception}")
                        val exception = task.exception as FirebaseAuthInvalidCredentialsException

                        if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL){
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                            //Log.d("MyLog", "Exception: ${exception.errorCode}")
                        } else if (exception.errorCode == FirebaseAuthConstants.ERROR_WRONG_PASSWORD) {
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_WRONG_PASSWORD, Toast.LENGTH_LONG).show()
                        }
                    } else if (task.exception is FirebaseAuthInvalidUserException){
                            val exception = task.exception as FirebaseAuthInvalidUserException
                            if (exception.errorCode == FirebaseAuthConstants.ERROR_USER_NOT_FOUND){
                                Toast.makeText(act, FirebaseAuthConstants.ERROR_USER_NOT_FOUND, Toast.LENGTH_LONG).show()
                            }

                        }
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
            .requestIdToken(act.getString(R.string.default_web_client_id)).requestEmail().build()
        return GoogleSignIn.getClient(act, gso)
    }

    fun signInWithGoogle(){
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent // интент для входа
        act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE) // ожидание результата - аккаунт, откуда берется токен
    }

    fun signOutGoogle(){
        getSignInClient().signOut()

    }

    fun signInFirebaseWithGoogle(token: String){ // вход по гугл аккаунту, из аккаунта берем токен и превращаем его в credential
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.mAuth.signInWithCredential(credential).addOnCompleteListener {
            task ->
                if (task.isSuccessful) {
                Toast.makeText(act, "Sign in done", Toast.LENGTH_LONG).show()
                    act.uiUpdate(task.result?.user)
            } else {
                    Log.d("MyLog", "Google Sign In Exception: ${task.exception}")
                }
        }
    }

    private fun linkEmailToGmail(email: String, password: String) { // функция для соединения почты и гугл аккаунты
        val credential = EmailAuthProvider.getCredential(email, password)
        if (act.mAuth.currentUser != null) {
            act.mAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act, act.resources.getString(R.string.link_done), Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(act, act.resources.getString(R.string.enter_to_gmail), Toast.LENGTH_LONG).show()
        }
    }


}