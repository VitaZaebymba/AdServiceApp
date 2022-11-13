package com.vita_zaebymba.adserviceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_content.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init(){
        var toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) //кнопка для бокового меню
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)//navigation view будет передавать нажатия на кнопки меню
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
                Toast.makeText(this, "Pressed to sign up", Toast.LENGTH_LONG).show()
            }
            R.id.id_sign_in -> {
                Toast.makeText(this, "Pressed to sign in", Toast.LENGTH_LONG).show()
            }
            R.id.id_sign_out -> {
                Toast.makeText(this, "Pressed to sign out", Toast.LENGTH_LONG).show()
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}