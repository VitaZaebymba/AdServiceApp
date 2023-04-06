package com.vita_zaebymba.adserviceapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.databinding.ActivityFilterBinding
import com.vita_zaebymba.adserviceapp.dialogs.DialogSpinnerHelper
import com.vita_zaebymba.adserviceapp.utils.CityHelper
import java.lang.StringBuilder

class FilterActivity : AppCompatActivity() {
    lateinit var binding: ActivityFilterBinding
    private val dialog = DialogSpinnerHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickSelectCountry()
        onClickSelectCity()
        onClickDone()
        actionBarSettings()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun onClickSelectCountry() = with(binding){
        tvChooseCountry.setOnClickListener{
            val listCountry = CityHelper.getAllCountries(this@FilterActivity)
            dialog.showSpinnerDialog(this@FilterActivity, listCountry, tvChooseCountry)
            if (tvChooseCity.text.toString() != getString(R.string.choose_city)){
                tvChooseCity.text = getString(R.string.choose_city)
            }
        }
    }

    private fun onClickSelectCity() = with(binding) {
        tvChooseCity.setOnClickListener {
            val selectedCountry = tvChooseCountry.text.toString()
            if (selectedCountry != getString(R.string.choose_country)) {
                val listCity = CityHelper.getAllCities(selectedCountry, this@FilterActivity)
                dialog.showSpinnerDialog(this@FilterActivity, listCity, tvChooseCity)
            } else {
                Toast.makeText(this@FilterActivity, R.string.no_country_selected, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun onClickDone() = with(binding) {
        btDone.setOnClickListener {
            Log.d("MyLog", "Filter: ${createFilter()}")
        }
    }

    private fun createFilter(): String = with(binding) {
        val sBuilder = StringBuilder()
        val arrayTempFilter = listOf(tvChooseCountry.text,
            tvChooseCity.text,
            editIndex.text,
            checkBoxWithSend.isChecked.toString())
        for ((i, s) in arrayTempFilter.withIndex()){
            if (s != getString(R.string.choose_country)
                && s != getString(R.string.choose_city)
                && s.isNotEmpty()) {
                sBuilder.append(s)
                if (i != arrayTempFilter.size - 1) sBuilder.append("_")
            }
        }
        return sBuilder.toString()
    }

    fun actionBarSettings(){
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }
}