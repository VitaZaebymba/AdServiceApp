package com.vita_zaebymba.adserviceapp.utils

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.vita_zaebymba.adserviceapp.activity.EditAdAct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImagePicker { // получаем картинки, чтобы потом показывать в списке и т.д.

    const val MAX_IMAGE_COUNT = 5
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    fun getImages(context: AppCompatActivity, imageCounter: Int, rCode: Int){
        val options  = Options.init()
            .setRequestCode(rCode) //Request code for activity results
            .setCount(imageCounter) ////Number of images to restrict selection c
            .setFrontfacing(false) //Front Facing camera on start
            .setSpanCount(4) //Span count for gallery min 1 & max 5
            .setMode(Options.Mode.Picture) //Option to select only pictures or video
            .setVideoDurationLimitinSeconds(30) //Duration for video recording
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientation
            .setPath("/pix/images") //Custom Path For media Storage

            Pix.start(context, options)
    }

    fun showSelectedImages(resultCode: Int, requestCode: Int, data: Intent?, edAct: EditAdAct){
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_IMAGES) {

            if (data != null) {

                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS) //если размер > 1, то 2 и больше картинок и отправляем во фрагмент, фрагмент запускает адаптер и адаптер заполняет RecyclerView

                if (returnValues?.size!! > 1 && edAct.chooseImageFragment == null) { // в первый раз выбираем картинки

                    edAct.openChooseImageFragment(returnValues) // returnValues - ссылки на картинки

                } else if (returnValues.size == 1 && edAct.chooseImageFragment == null) { // выбор одной картинки

                    CoroutineScope(Dispatchers.Main).launch{

                        edAct.rootElement.pBarLoad.visibility = View.VISIBLE
                        val bitMapArray = ImageManager.imageResize(returnValues) as ArrayList<Bitmap>
                        edAct.rootElement.pBarLoad.visibility = View.GONE
                        edAct.imageAdapter.update(bitMapArray)
                    }


                } else if (edAct.chooseImageFragment != null) { // пользователь выбирал картинки раньше

                    edAct.chooseImageFragment?.updateAdapter(returnValues)
                }

            }

        } else if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_SINGLE_IMAGE){
            if (data != null) {

                val uris = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                edAct.chooseImageFragment?.setSingleImage(uris?.get(0)!!, edAct.editImagePosition)
            }
        }
    }

}