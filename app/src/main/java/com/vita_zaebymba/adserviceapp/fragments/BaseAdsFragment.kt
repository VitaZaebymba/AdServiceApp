package com.vita_zaebymba.adserviceapp.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.fragments.InterAdsClose

open class BaseAdsFragment: Fragment(), InterAdsClose { // класс для показа рекламного баннера

    lateinit var adView: AdView
    var interAd: InterstitialAd? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
    }

    override fun onCreate(savedInstanceState: Bundle?) { // открываем фрагмент
        super.onCreate(savedInstanceState)
        loadInterAd()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }

    private fun initAds(){
        MobileAds.initialize(activity as Activity)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun loadInterAd(){ // загрузка рекламы
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context as Activity, getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback(){

                override fun onAdLoaded(ad: InterstitialAd) {
                    interAd = ad //загруженная реклама
                }
            })

    }

    fun showInterAd(){
        if (interAd != null){
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){ // callback будет следить за рекламой, которая показалась
                override fun onAdDismissedFullScreenContent() {
                    onClose()
                }
                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    onClose()
                }
            }
            interAd?.show(activity as Activity)
        } else {
            onClose()
        }
    }

    override fun onClose() {}

}