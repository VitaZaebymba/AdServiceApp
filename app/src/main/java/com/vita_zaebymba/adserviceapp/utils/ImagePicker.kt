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

    fun launcher(edAct: EditAdAct,  imageCounter: Int){
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

    private fun closePixFrag(edAct: EditAdAct){
        val fList = edAct.supportFragmentManager.fragments
        fList.forEach {
            if (it.isVisible) edAct.supportFragmentManager.beginTransaction().remove(it).commit() // закрытие фрагмента после выбора фото
        }
    }


    fun getMultiSelectedImages(edAct: EditAdAct, uris: List<Uri>) { // фнукция будет сразу принимать ссылки и активити и их обрабатывать, List - это выбранные фото, которые передались
        if (uris.size > 1 && edAct.chooseImageFragment == null) { // в первый раз выбираем картинки
            edAct.openChooseImageFragment(uris as ArrayList<Uri>) // uris - ссылки на картинки

        } else if (edAct.chooseImageFragment != null) {
            edAct.chooseImageFragment?.updateAdapter(uris as ArrayList<Uri>)

        } else if (uris.size == 1 && edAct.chooseImageFragment == null) {
            CoroutineScope(Dispatchers.Main).launch {
                edAct.rootElement.pBarLoad.visibility = View.VISIBLE
                val bitMapArray = ImageManager.imageResize(uris as ArrayList<Uri>, edAct) as ArrayList<Bitmap>
                edAct.rootElement.pBarLoad.visibility = View.GONE
                edAct.imageAdapter.update(bitMapArray)
                }

        }
    }




    fun getLauncherForSingleImage(edAct: EditAdAct): ActivityResultLauncher<Intent> {
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

           /* if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {

                    val uris = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    edAct.chooseImageFragment?.setSingleImage(uris?.get(0)!!, edAct.editImagePosition)
                }
            }*/
        }
    }

}