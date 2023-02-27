package com.vita_zaebymba.adserviceapp.utils

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.vita_zaebymba.adserviceapp.activity.EditAdAct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImagePicker { // получаем картинки, чтобы потом показывать в списке и т.д.

    const val MAX_IMAGE_COUNT = 5
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    private fun getOptions(imageCounter: Int): Options{
        val options  = Options.init()
            .setCount(imageCounter) ////Number of images to restrict selection c
            .setFrontfacing(false) //Front Facing camera on start
            .setMode(Options.Mode.Picture) //Option to select only pictures or video
            .setVideoDurationLimitinSeconds(30) //Duration for video recording
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientation
            .setPath("/pix/images") //Custom Path For media Storage

        return options
    }

    fun launcher(edAct: EditAdAct, launcher: ActivityResultLauncher<Intent>?, imageCounter: Int){
        PermUtil.checkForCamaraWritePermissions(edAct){
            val intent = Intent(edAct, Pix::class.java).apply { // запуск библиотеки Pix
                putExtra("options", getOptions(imageCounter))
            }
            launcher?.launch(intent) // слушатель getLauncherForMultiImages выдает лаунчер
        }

    }


    fun getLauncherForMultiImages(edAct: EditAdAct): ActivityResultLauncher<Intent>{ //лаунчер для получения нескольких картинок. Слушатель, который следит за результатом, полученный из активити
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->

            if (result.resultCode == AppCompatActivity.RESULT_OK) {

                if (result.data != null) {

                    val returnValues = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS) //если размер > 1, то 2 и больше картинок и отправляем во фрагмент, фрагмент запускает адаптер и адаптер заполняет RecyclerView

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

            }
        }
    }


    fun getLauncherForSingleImage(edAct: EditAdAct): ActivityResultLauncher<Intent> {
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {

                    val uris = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    edAct.chooseImageFragment?.setSingleImage(uris?.get(0)!!, edAct.editImagePosition)
                }
            }
        }
    }

}