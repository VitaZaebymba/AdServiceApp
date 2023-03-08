package com.vita_zaebymba.adserviceapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vita_zaebymba.adserviceapp.model.Ad
import com.vita_zaebymba.adserviceapp.model.DatabaseManager

class FirebaseViewModel: ViewModel() {
    private val dbManager = DatabaseManager()
    val liveAdsData = MutableLiveData<ArrayList<Ad>>() // посредник для обновления view

    fun loadAllAds(){
        dbManager.getAllAds(object: DatabaseManager.ReadDataCallback{
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }

        })
    }

    fun loadMyAds(){
        dbManager.getMyAds(object: DatabaseManager.ReadDataCallback{
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }

        })
    }

}