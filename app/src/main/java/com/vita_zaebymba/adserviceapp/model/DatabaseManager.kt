package com.vita_zaebymba.adserviceapp.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query

import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DatabaseManager {
    val db = Firebase.database.getReference(MAIN_NODE) // получение инстанции бд
    val dbStorage = Firebase.storage.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishAd(ad: Ad, finishListener: FinishWorkListener) {
        if (auth.uid != null) db.child(ad.key ?: "Empty").child(auth.uid!!).child(AD_NODE)
            .setValue(ad).addOnCompleteListener { // ожидание окончания записи в бд

            val adFilter = AdFilter(ad.time, "${ad.category}_${ad.time}")
            db.child(ad.key ?: "Empty").child(FILTER_NODE).setValue(adFilter)
                .addOnCompleteListener {
                    finishListener.onFinish()
                }
        }
    }

    fun adViewed(ad: Ad){ // объявляение просмотрено, 4 шаг
        var counter = ad.viewsCounter!!.toInt()
        counter++
        if (auth.uid != null) {
            db.child(ad.key ?: "Empty")
                .child(INFO_NODE)
                .setValue(InfoItem(counter.toString(), ad.emailCounter, ad.callsCounter))
        }
    }

    fun onFavClick(ad: Ad, listener: FinishWorkListener){ // определяет, какую функцию запустить: addToFavs или removeFromFavs
        if (ad.isFav) {
            removeFromFavs(ad, listener)
        } else {
            addToFavs(ad, listener)
        }
    }

    private fun addToFavs(ad: Ad, listener: FinishWorkListener){ // добавление в избранное
        ad.key?.let {
            auth.uid?.let { uid -> db.child(it).child(FAVS_NODE).child(uid).setValue(uid).addOnCompleteListener { // db - основной путь, it - key, далее - избранное (favs)
                if(it.isSuccessful) listener.onFinish()
            } }
        }
    }

    private fun removeFromFavs(ad: Ad, listener: FinishWorkListener){ // удаление из избранного
        ad.key?.let {
            auth.uid?.let { uid -> db.child(it).child(FAVS_NODE).child(uid).removeValue().addOnCompleteListener { // db - основной путь, it - key, далее - избранное (favs)
                if(it.isSuccessful) listener.onFinish()
            } }
        }
    }


    fun getMyAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid) // выдать все объявления, принадлежащие uid
        readDataFromDb(query, readDataCallback)
    }

    fun getMyFavs(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild( "/favs/${auth.uid}").equalTo(auth.uid) // выдать все объявления, принадлежащие uid
        readDataFromDb(query, readDataCallback)
    }

    fun getAllAds(lastTime: String, readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(GET_ALL_ADS).startAfter(lastTime).limitToFirst(ADS_LIMIT)
        readDataFromDb(query, readDataCallback)
    }

    fun getAllAdsFromCats(lastCatTime: String, readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(GET_ALL_ADS_FROM_CAT).startAfter(lastCatTime).limitToFirst(ADS_LIMIT)
        readDataFromDb(query, readDataCallback)
    }

    fun  deleteAd(ad: Ad, listener: FinishWorkListener) {
        if (ad.key == null || ad.uid == null) return
        db.child(ad.key).child(ad.uid).removeValue().addOnCompleteListener { // ключ объявления - название узла, где находится объявление, операция асинхронная
            if (it.isSuccessful) listener.onFinish()
        }
    }



    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?){ // читаем данные из бд
        query.addListenerForSingleValueEvent(object: ValueEventListener{ // query - путь, откуда считываем данные

            override fun onDataChange(snapshot: DataSnapshot) { // в snapshot находятся считанные данные
                val adArray = ArrayList<Ad>()
                for (item in snapshot.children) {

                    var ad: Ad? = null

                    item.children.forEach{ // цикл внутри узла key
                        if (ad == null) ad = it.child(AD_NODE).getValue(Ad::class.java)
                    }
                    val infoItem = item.child(INFO_NODE).getValue(InfoItem::class.java)

                    val favCounter = item.child(FAVS_NODE).childrenCount // кол-во элементов, которые находятся на этом пути
                    val isFav = auth.uid?.let { item.child(FAVS_NODE).child(it).getValue(String::class.java) }
                    ad?.isFav = isFav != null // узнаем, объявление в избранном или нет

                    ad?.favCounter = favCounter.toString()

                    // перезагружаем данные в объявление
                    ad?.viewsCounter = infoItem?.viewsCounter ?: "0"
                    ad?.emailCounter = infoItem?.emailCounter ?: "0"
                    ad?.callsCounter = infoItem?.callsCounter ?: "0"
                    if (ad != null ) adArray.add(ad!!) // заполняем массив объявлениеями

                }
                readDataCallback?.readData(adArray) // отправляем список на адаптер

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    interface ReadDataCallback {
        fun readData(list: ArrayList<Ad>)
    }

    interface FinishWorkListener { // чтобы знать, когда запись прошла
        fun onFinish()
    }

    companion object {
        const val AD_NODE = "ad"
        const val FILTER_NODE = "adFilter"
        const val MAIN_NODE = "main"
        const val INFO_NODE = "info"
        const val FAVS_NODE = "favs"
        const val ADS_LIMIT = 2

        const val GET_ALL_ADS = "/adFilter/time"
        const val GET_ALL_ADS_FROM_CAT = "/adFilter/catTime"
    }

}