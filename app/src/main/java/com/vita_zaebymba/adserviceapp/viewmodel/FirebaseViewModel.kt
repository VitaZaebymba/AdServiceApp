package com.vita_zaebymba.adserviceapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vita_zaebymba.adserviceapp.model.Ad
import com.vita_zaebymba.adserviceapp.model.DatabaseManager

class FirebaseViewModel: ViewModel() {
    private val dbManager = DatabaseManager()
    val liveAdsData = MutableLiveData<ArrayList<Ad>?>() // посредник для обновления view

    fun loadAllAds(){
        dbManager.getAllAds(object: DatabaseManager.ReadDataCallback{
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }

        })
    }

    fun adViewed(ad: Ad) { // просмотр объявления, 3 шаг
        dbManager.adViewed(ad)
    }

    fun loadMyAds(){
        dbManager.getMyAds(object: DatabaseManager.ReadDataCallback{
            override fun readData(list: ArrayList<Ad>) {
                liveAdsData.value = list
            }

        })
    }

    fun deleteItem(ad: Ad){
        dbManager.deleteAd(ad, object: DatabaseManager.FinishWorkListener {
            override fun onFinish() {
                val updatedList = liveAdsData.value // берем старый список
                updatedList?.remove(ad) // после удаления в бд, удаляем и в адаптере
                liveAdsData.postValue(updatedList)
            }
        })
    }

}