package com.vita_zaebymba.adserviceapp.utils

import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix

object ImagePicker { // получаем картинки, чтобы потом показывать в списке и т.д.
    const val REQUEST_CODE_GET_IMAGES = 999
    fun getImages(context: AppCompatActivity){
        val options  = Options.init()
            .setRequestCode(REQUEST_CODE_GET_IMAGES) //Request code for activity results
            .setCount(3) ////Number of images to restrict selection c
            .setFrontfacing(false) //Front Facing camera on start
            .setSpanCount(4) //Span count for gallery min 1 & max 5
            .setMode(Options.Mode.Picture) //Option to select only pictures or video
            .setVideoDurationLimitinSeconds(30) //Duration for video recording
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientation
            .setPath("/pix/images") //Custom Path For media Storage

            Pix.start(context, options)
    }

}