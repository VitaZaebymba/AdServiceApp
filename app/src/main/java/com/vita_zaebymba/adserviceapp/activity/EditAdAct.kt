package com.vita_zaebymba.adserviceapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.databinding.ActivityEditAdBinding
import com.vita_zaebymba.adserviceapp.dialogs.DialogSpinnerHelper
import com.vita_zaebymba.adserviceapp.utils.CityHelper

class EditAdAct : AppCompatActivity() {
    lateinit var rootElement: ActivityEditAdBinding
    private val dialog = DialogSpinnerHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()

    }

    private fun init(){

    }

    //OnClicks
    fun onClickSelectCountry(view: View){
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry)
    }

    fun onClickSelectCity(view: View){
        val selectedCountry = rootElement.tvChooseCountry.text.toString()
        if (selectedCountry != getString(R.string.choose_country)){
            val listCity = CityHelper.getAllCities(selectedCountry, this)
            dialog.showSpinnerDialog(this, listCity)
        } else {
            Toast.makeText(this, R.string.no_country_selected, Toast.LENGTH_LONG).show()
        }

    }

}