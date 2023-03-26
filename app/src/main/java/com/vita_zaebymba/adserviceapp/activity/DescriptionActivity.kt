package com.vita_zaebymba.adserviceapp.activity

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vita_zaebymba.adserviceapp.adapters.ImageAdapter
import com.vita_zaebymba.adserviceapp.databinding.ActivityDescriptionBinding
import com.vita_zaebymba.adserviceapp.model.Ad
import com.vita_zaebymba.adserviceapp.utils.ImageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DescriptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityDescriptionBinding
    lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        adapter = ImageAdapter()
        binding.apply {
            viewPager.adapter = adapter
        }
        getIntentFromMainAct()
    }

    private fun getIntentFromMainAct(){
        val ad = intent.getSerializableExtra(AD) as Ad
        fillImageArray(ad)
    }

    private fun fillImageArray(ad: Ad) { // заполнение массива ссылками из getBitmapFromUri (class ImageManager)
        val listUris = listOf(ad.mainImage, ad.image2, ad.image3)
        CoroutineScope(Dispatchers.Main).launch {
            val bitmapList = ImageManager.getBitmapFromUris(listUris)
            adapter.update(bitmapList as ArrayList<Bitmap>)
        }
    }

    companion object{
        const val AD = "AD"
    }

}