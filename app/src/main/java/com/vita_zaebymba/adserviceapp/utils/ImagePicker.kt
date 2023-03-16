package com.vita_zaebymba.adserviceapp.utils

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.activity.EditAdAct
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImagePicker { // получаем картинки, чтобы потом показывать в списке и т.д.

    const val MAX_IMAGE_COUNT = 5
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    private fun getOptions(imageCounter: Int): Options {
        val options  = Options().apply {
            count = imageCounter //Number of images to restrict selection
            isFrontFacing = false //Front Facing camera on start
            mode = Mode.Picture //Option to select only pictures or video
            path = "/pix/images"
        }
        return options
    }

    fun getMultiImages (edAct: EditAdAct, imageCounter: Int){ // открывает фрагмент от библиотеки Pix
        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)){ result ->// повяление фрагмента для выбора фото вместо place_holder
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    getMultiSelectedImages(edAct, result.data) // передаем активити и список ссылок ContentResolver от библиотеки Pix, это уже ссылки не на файл, а на content
                    closePixFrag(edAct)
                }
                else -> {}
            }
        }

    }

    fun getSingleImages (edAct: EditAdAct){ // открывает фрагмент от библиотеки Pix
        val f = edAct.chooseImageFragment
        edAct.addPixToActivity(R.id.place_holder, getOptions(1)){ result ->// повяление фрагмента для выбора фото вместо place_holder
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                   edAct.chooseImageFragment = f
                   openChooseImageFrag(edAct, f!!)
                   singleImage(edAct, result.data[0])
                }
                else -> {}
            }
        }

    }

    private fun openChooseImageFrag(edAct: EditAdAct, f: Fragment) {
        edAct.supportFragmentManager.beginTransaction().replace(R.id.place_holder, f).commit() // открывает фрагмент, который был в памяти
    }



    private fun closePixFrag(edAct: EditAdAct){
        val fList = edAct.supportFragmentManager.fragments
        fList.forEach {
            if (it.isVisible) edAct.supportFragmentManager.beginTransaction().remove(it).commit() // закрытие фрагмента после выбора фото
        }
    }


    fun getMultiSelectedImages(edAct: EditAdAct, uris: List<Uri>) { // фнукция будет сразу принимать ссылки и активити и их обрабатывать, List - это выбранные фото, которые передались
        if (uris.size > 1 && edAct.chooseImageFragment == null) { // в первый раз выбираем картинки
            edAct.openChooseImageFragment(uris as ArrayList<Uri>) // uris - ссылки на картинки, открыть новый фрагмент

        } else if (edAct.chooseImageFragment != null) {
            edAct.chooseImageFragment?.updateAdapter(uris as ArrayList<Uri>) // во фрагмент, который был открыт, передать туда новые картинки

        } else if (uris.size == 1 && edAct.chooseImageFragment == null) { // либо взять картинки, которые уже были на edAct и переедать во фрагмент

            CoroutineScope(Dispatchers.Main).launch {
                edAct.rootElement.pBarLoad.visibility = View.VISIBLE
                val bitMapArray = ImageManager.imageResize(uris as ArrayList<Uri>, edAct) as ArrayList<Bitmap>
                edAct.rootElement.pBarLoad.visibility = View.GONE
                edAct.imageAdapter.update(bitMapArray)
            }
        }
    }




   private  fun singleImage(edAct: EditAdAct, uri: Uri) {
       edAct.chooseImageFragment?.setSingleImage(uri, edAct.editImagePosition)

    }

}