package com.vita_zaebymba.adserviceapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.adapters.ImageAdapter
import com.vita_zaebymba.adserviceapp.databinding.ActivityEditAdBinding
import com.vita_zaebymba.adserviceapp.dialogs.DialogSpinnerHelper
import com.vita_zaebymba.adserviceapp.fragments.FragmentCloseInterface
import com.vita_zaebymba.adserviceapp.fragments.ImageListFragment
import com.vita_zaebymba.adserviceapp.utils.CityHelper
import com.vita_zaebymba.adserviceapp.utils.ImageManager
import com.vita_zaebymba.adserviceapp.utils.ImagePicker
import java.util.ArrayList

class EditAdAct : AppCompatActivity(), FragmentCloseInterface {
    private var chooseImageFragment: ImageListFragment? = null
    lateinit var rootElement: ActivityEditAdBinding
    private val dialog = DialogSpinnerHelper()
    private var isImagesPermissionGranted = false
    private lateinit var imageAdapter: ImageAdapter
    var editImagePosition = 0 //позиция картинки, которую хотим изменить (для редактирования фото)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //запускается при нажатии на кнопку галочку для добавления картинок
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_IMAGES) {

            if (data != null) {

                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS) //если размер > 1, то 2 и больше картинок и отправляем во фрагмент, фрагмент запускает адаптер и адаптер заполняет RecyclerView

                if (returnValues?.size!! > 1 && chooseImageFragment == null) {
                    openChooseImageFragment(returnValues) // returnValues - ссылки на картинки

                } else if (returnValues.size == 1 && chooseImageFragment == null) { // выбор одной картинки

                   //imageAdapter.update(returnValues)
                    val tempList = ImageManager.getImageSize(returnValues[0])
                    Log.d("MyLog", "Image width: ${tempList[0]}")
                    Log.d("MyLog", "Image height: ${tempList[1]}")

                } else if (chooseImageFragment != null) {

                    chooseImageFragment?.updateAdapter(returnValues)
                }

            }

        } else if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE){
            if (data != null) {

                val uris = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                chooseImageFragment?.setSingleImage(uris?.get(0)!!, editImagePosition)
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
                    ImagePicker.getImages(this, 5, ImagePicker.REQUEST_CODE_GET_IMAGES)
                } else {
                    isImagesPermissionGranted = false
                    Toast.makeText(this, "Approve permission to open Pix ImagePicker", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }


    private fun init(){
        imageAdapter = ImageAdapter()
        rootElement.vpImages.adapter = imageAdapter

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
       if (imageAdapter.mainArray.size == 0){ // если нет фото, открываем выбор картинки, если есть фото, то открываем фрагмент с фото
           ImagePicker.getImages(this, 5, ImagePicker.REQUEST_CODE_GET_IMAGES)
       } else{
           openChooseImageFragment(imageAdapter.mainArray)
       }

    }

    override fun onFragmentClose(list: ArrayList<String>) {
        rootElement.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFragment = null
    }

    private fun openChooseImageFragment(newList: ArrayList<String>){
        chooseImageFragment = ImageListFragment(this, newList)
        rootElement.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFragment!!) //интерфейс передадим во фрагмент через конструктор
        fm.commit()
    }

}