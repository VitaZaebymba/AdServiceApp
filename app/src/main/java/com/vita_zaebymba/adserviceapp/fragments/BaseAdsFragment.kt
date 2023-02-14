package com.vita_zaebymba.adserviceapp.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.databinding.ListImageFragmentBinding

open class BaseAdsFragment: Fragment(), InterAdsClose { // класс для показа рекламного баннера

    lateinit var adView: AdView
    var interAd: InterstitialAd? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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

    private fun loadInterAd(){
        val adRequest = AdRequest.Builder().build()
        context?.let {
            InterstitialAd.load(it, getString(R.string.inter_id), adRequest, object : InterstitialAdLoadCallback(){

                override fun onAdLoaded(ad: InterstitialAd) {
                    interAd = ad //загруженная реклама
                }
            })
        }
    }

    fun showInterAd(){

        if (interAd != null){
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){ // callback будет следить за рекламой, которая показалась
                override fun onAdDismissedFullScreenContent() {
                    onClose()
                }

            }
        }

    }

    override fun onClose() {}

}