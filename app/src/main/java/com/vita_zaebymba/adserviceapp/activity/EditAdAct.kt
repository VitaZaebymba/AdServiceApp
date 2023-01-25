package com.vita_zaebymba.adserviceapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.databinding.ActivityEditAdBinding
import com.vita_zaebymba.adserviceapp.dialogs.DialogSpinnerHelper
import com.vita_zaebymba.adserviceapp.utils.CityHelper

class EditAdAct : AppCompatActivity() {
    private lateinit var rootElement: ActivityEditAdBinding
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

}