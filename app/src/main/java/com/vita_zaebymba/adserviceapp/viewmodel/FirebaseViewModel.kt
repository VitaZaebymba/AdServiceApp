package com.vita_zaebymba.adserviceapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vita_zaebymba.adserviceapp.model.Ad
import com.vita_zaebymba.adserviceapp.model.DatabaseManager

class FirebaseViewModel: ViewModel() {
    private val dbManager = DatabaseManager()
    val liveAdsData = MutableLiveData<ArrayList<Ad>>() // посредник для обновления view

    public fun loadAllAds(){
        dbManager.readDataFromDb(object: DatabaseManager.ReadDataCallback{
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }

        })
    }
}