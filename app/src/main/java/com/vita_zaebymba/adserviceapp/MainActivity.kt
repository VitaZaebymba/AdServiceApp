package com.vita_zaebymba.adserviceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.vita_zaebymba.adserviceapp.databinding.ActivityMainBinding
import com.vita_zaebymba.adserviceapp.dialoghelper.DialogHelper


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var rootElement: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }

    private fun init(){
        var toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout, rootElement.toolbarMainContent.toolbar, R.string.open, R.string.close) //кнопка для бокового меню
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener(this) //navigation view будет передавать нажатия на кнопки меню
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

                dialogHelper.createSignDialog()

            }
            R.id.id_sign_in -> {
                Toast.makeText(this, "Pressed to sign in", Toast.LENGTH_LONG).show()
            }
            R.id.id_sign_out -> {
                Toast.makeText(this, "Pressed to sign out", Toast.LENGTH_LONG).show()
            }

        }
        rootElement.drawerLayout.closeDrawer(GravityCompat.START) //меню сворачивается после нажатия на раздел
        return true
    }

}