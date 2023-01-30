package com.vita_zaebymba.adserviceapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.databinding.ActivityEditAdBinding
import com.vita_zaebymba.adserviceapp.dialogs.DialogSpinnerHelper
import com.vita_zaebymba.adserviceapp.utils.CityHelper
import com.vita_zaebymba.adserviceapp.utils.ImagePicker

class EditAdAct : AppCompatActivity() {
    lateinit var rootElement: ActivityEditAdBinding
    private val dialog = DialogSpinnerHelper()
    private var isImagesPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_IMAGES) {
            if (data != null){
                val returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                Log.d("MyLog", "Image: ${returnValue?.get(0)}")
            }

        }
    }

    override fun onRequestPermissionsResult( // Доступ к памяти и камере
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                //If request is cancelled, the result arrays are empty
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    ImagePicker.getImages(this)
                } else {
                    isImagesPermissionGranted = false
                    Toast.makeText(this, "Approve permission to open Pix ImagePicker", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }


    private fun init(){

    }

    //OnClicks
    fun onClickSelectCountry(view: View){
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvChooseCountry)
        if (rootElement.tvChooseCity.text.toString() != getString(R.string.choose_city)){
            rootElement.tvChooseCity.text = getString(R.string.choose_city)
        }
    }

    fun onClickSelectCity(view: View){
        val selectedCountry = rootElement.tvChooseCountry.text.toString()
        if (selectedCountry != getString(R.string.choose_country)){
            val listCity = CityHelper.getAllCities(selectedCountry, this)
            dialog.showSpinnerDialog(this, listCity, rootElement.tvChooseCity)
        } else {
            Toast.makeText(this, R.string.no_country_selected, Toast.LENGTH_LONG).show()
        }

    }

    fun onClickGetImages(view: View){
        ImagePicker.getImages(this)

    }

}