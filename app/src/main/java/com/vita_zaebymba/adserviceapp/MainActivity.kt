package com.vita_zaebymba.adserviceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.vita_zaebymba.adserviceapp.databinding.ActivityMainBinding
import com.vita_zaebymba.adserviceapp.dialoghelper.DialogConst
import com.vita_zaebymba.adserviceapp.dialoghelper.DialogHelper
import com.vita_zaebymba.adserviceapp.dialoghelper.GoogleAccConst


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var tvAccount: TextView

    private lateinit var rootElement: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE){
            //Log.d("MyLog", "Sign in Result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data) // передаем интент аккаунта, по которому зашли
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                }

            } catch (e:ApiException){
                Log.d("MyLog", "Api error: ${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun init(){
        var toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout, rootElement.toolbarMainContent.toolbar, R.string.open, R.string.close) //кнопка для бокового меню
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener(this) //navigation view будет передавать нажатия на кнопки меню
        tvAccount = rootElement.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail) //отображение текста в header
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.id_my_ads -> {
                Toast.makeText(this, "Pressed to ads", Toast.LENGTH_LONG).show()
            }
            R.id.id_car -> {
                Toast.makeText(this, "Pressed to cars", Toast.LENGTH_LONG).show()
            }
            R.id.id_pc -> {
                Toast.makeText(this, "Pressed to pc", Toast.LENGTH_LONG).show()
            }
            R.id.id_phones -> {
                Toast.makeText(this, "Pressed to phones", Toast.LENGTH_LONG).show()
            }
            R.id.id_dm -> {
                Toast.makeText(this, "Pressed to dm", Toast.LENGTH_LONG).show()
            }
            R.id.id_sign_up -> {

                dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE) // создание диалогового окна с регистрацией

            }
            R.id.id_sign_in -> {

                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)

            }
            R.id.id_sign_out -> {
                uiUpdate(null)
                mAuth.signOut()
            }

        }
        rootElement.drawerLayout.closeDrawer(GravityCompat.START) //меню сворачивается после нажатия на раздел
        return true
    }

    fun uiUpdate(user:FirebaseUser?){ // шапка бокового меню

        tvAccount.text = if (user == null) {
            resources.getString(R.string.not_reg)
        } else {
            user.email
        }

    }

}