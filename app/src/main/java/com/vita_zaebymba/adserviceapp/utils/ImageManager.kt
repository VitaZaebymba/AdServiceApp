package com.vita_zaebymba.adserviceapp.utils

import android.graphics.BitmapFactory

object ImageManager {

    fun getImageSize(uri: String): List<Int>{

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true // берем только края картинки
        }
        BitmapFactory.decodeFile(uri, options)
        return listOf(options.outWidth, options.outHeight)
    }
}