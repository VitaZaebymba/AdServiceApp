package com.vita_zaebymba.adserviceapp.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.fxn.utility.PermUtil
import com.vita_zaebymba.adserviceapp.MainActivity
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.adapters.ImageAdapter
import com.vita_zaebymba.adserviceapp.model.Ad
import com.vita_zaebymba.adserviceapp.databinding.ActivityEditAdBinding
import com.vita_zaebymba.adserviceapp.dialogs.DialogSpinnerHelper
import com.vita_zaebymba.adserviceapp.fragments.FragmentCloseInterface
import com.vita_zaebymba.adserviceapp.fragments.ImageListFragment
import com.vita_zaebymba.adserviceapp.model.DatabaseManager
import com.vita_zaebymba.adserviceapp.utils.CityHelper
import com.vita_zaebymba.adserviceapp.utils.ImagePicker
import java.util.ArrayList

class EditAdAct : AppCompatActivity(), FragmentCloseInterface {
    var chooseImageFragment: ImageListFragment? = null
    lateinit var rootElement: ActivityEditAdBinding
    private val dialog = DialogSpinnerHelper()
    private var isImagesPermissionGranted = false
    lateinit var imageAdapter: ImageAdapter
    private val dbManager = DatabaseManager()
    var launcherMultiSelectImage: ActivityResultLauncher<Intent>? = null
    var launcherSingleSelectImage: ActivityResultLauncher<Intent>? = null
    var editImagePosition = 0 //позиция картинки, которую хотим изменить (для редактирования фото)
    private var isEditState = false
    private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
        checkEditState()
    }

    private fun checkEditState(){
        isEditState = isEditState() // проверяя эту переменную, если true - то это объявление для редактирования и при нажатии на "обубликовать" объявление обновляется
        if (isEditState) {
            ad = intent.getSerializableExtra(MainActivity.ADS_DATA) as Ad // переменная будет доступна на уровне всего класса и из объявления, которое редактируем, сможем достать ключ, чтобы по нему обновить объявление
            if (ad != null) fillViews(ad!!)
        }
    }

    private fun isEditState() : Boolean { // проверка состояния для редактирования
        return intent.getBooleanExtra(MainActivity.EDIT_STATE, false)
    }

    private fun fillViews(ad: Ad) = with(rootElement) { // заполнение объявления, редактирование
        tvChooseCountry.text = ad.country
        tvChooseCity.text = ad.city
        editTel.setText(ad.tel)
        editIndex.setText(ad.index)
        checkBoxWithSend.isChecked = ad.delivery.toBoolean()

        tvCategory.text = ad.category
        tvTitleWrite.setText(ad.title)
        editPrice.setText(ad.price)
        editTextDescription.setText(ad.description)

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
                    //ImagePicker.getImages(this, 5, ImagePicker.REQUEST_CODE_GET_IMAGES)
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

        launcherMultiSelectImage = ImagePicker.getLauncherForMultiImages(this) // ссылку на созданный коллбак сохраняем в переменной
        launcherSingleSelectImage = ImagePicker.getLauncherForSingleImage(this)

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

    fun onClickSelectCategory(view: View){
        val listCity = resources.getStringArray(R.array.category).toMutableList() as ArrayList
        dialog.showSpinnerDialog(this, listCity, rootElement.tvCategory)
    }


    fun onClickGetImages(view: View){
       if (imageAdapter.mainArray.size == 0){ // если нет фото, открываем выбор картинки, если есть фото, то открываем фрагмент с фото
           ImagePicker.launcher(this, launcherMultiSelectImage, 5) // результат будет приходить в getLauncherForMultiImages
       } else{
           openChooseImageFragment(null)
           chooseImageFragment?.updateAdapterFromEdit(imageAdapter.mainArray)
       }

    }

    fun onClickPublish(view: View){
        val adTemp = fillAd() // заполнили ad
        if(isEditState){
            dbManager.publishAd(adTemp.copy(key = ad?.key), onPublishFinish()) // передаем копию с измениями, асинхронная операция
        } else {
            dbManager.publishAd(adTemp, onPublishFinish())
        }
    }

    private fun onPublishFinish(): DatabaseManager.FinishWorkListener{
        return object: DatabaseManager.FinishWorkListener{
            override fun onFinish() {
                finish() // закрываем активити, зная, что данные все опубликовались
            }

        }
    }

    private fun fillAd(): Ad{
        val ad: Ad
        rootElement.apply {
            ad = Ad(tvTitleWrite.text.toString(),
                tvChooseCountry.text.toString(),
                tvChooseCity.text.toString(),
                editTel.text.toString(),
                editIndex.text.toString(),
                checkBoxWithSend.isChecked.toString(),
                tvCategory.text.toString(),
                editPrice.text.toString(),
                editTextDescription.text.toString(),
                dbManager.db.push().key, //генарация уникального ключа для пути
                dbManager.auth.uid,
                "0"
                )
        }
        return ad
    }


    override fun onFragmentClose(list: ArrayList<Bitmap>) {
        rootElement.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFragment = null
    }

    fun openChooseImageFragment(newList: ArrayList<String>?){
        chooseImageFragment = ImageListFragment(this, newList)
        rootElement.scrollViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFragment!!) //интерфейс передадим во фрагмент через конструктор
        fm.commit()
    }

}