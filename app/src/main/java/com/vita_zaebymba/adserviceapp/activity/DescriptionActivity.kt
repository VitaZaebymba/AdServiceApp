package com.vita_zaebymba.adserviceapp.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.vita_zaebymba.adserviceapp.R
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
    private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        binding.fbTel.setOnClickListener { call() }
        binding.fbEmail.setOnClickListener { sendEmail() }
    }

    private fun init(){ // инициализируем адаптер
        adapter = ImageAdapter()
        binding.apply {
            viewPager.adapter = adapter
        }
        getIntentFromMainAct()
    }

    private fun getIntentFromMainAct(){
        ad = intent.getSerializableExtra(AD) as Ad // получаем объявление
        if(ad != null) updateUI(ad!!)
    }


    private fun updateUI(ad: Ad) {
        ImageManager.fillImageArray(ad, adapter) // заполняем картинками
        fillTextViews(ad)
    }

    private fun fillTextViews(ad:Ad) = with(binding) {
        tvTitleDescription.text = ad.title
        tvEmailD.text = ad.email
        tvPriceD.text = ad.price
        tvTelD.text = ad.tel
        tvCountryD.text = ad.country
        tvCityD.text = ad.city
        tvIndexD.text = ad.index
        tvDeliveryD.text = isDelivery(ad.delivery.toBoolean())
        tvDescription.text = ad.description

    }

    private fun isDelivery(withDelivery: Boolean): String {
        val yes = getString(R.string.yes)
        val no = getString(R.string.no)
        return if (withDelivery) yes else no

    }

    companion object{
        const val AD = "AD"
    }

    private fun call() { // функция для звонка продавцу
        val callUri = "tel:${ad?.tel}"
        val iCall = Intent(Intent.ACTION_DIAL) // для открытия приложения для звонков
        iCall.data = callUri.toUri()
        startActivity(iCall)
    }

    private fun sendEmail() {
        val iSendEmail = Intent(Intent.ACTION_SEND)
        iSendEmail.type = "message/rfc822"
        iSendEmail.apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(ad?.email))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.extra_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.extra_text))
        }
        try {
            startActivity(Intent.createChooser(iSendEmail, getString(R.string.open_email)))
        } catch (e: ActivityNotFoundException) {

        }
    }

}