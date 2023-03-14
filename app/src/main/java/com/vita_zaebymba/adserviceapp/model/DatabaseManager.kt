package com.vita_zaebymba.adserviceapp.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query

import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class DatabaseManager {
    val db = Firebase.database.getReference(MAIN_NODE) // получение инстанции бд
    val auth = Firebase.auth

    fun publishAd(ad: Ad, finishListener: FinishWorkListener){
        if (auth.uid != null) {
            db.child(ad.key ?: "Empty").child(auth.uid!!).child(AD_NODE).setValue(ad).addOnCompleteListener { // ожидание окончания записи в бд
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


    fun getMyAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid) // выдать все объявления, принадлежащие uid
        readDataFromDb(query, readDataCallback)
    }

    fun getAllAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "ad/price")
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

    interface FinishWorkListener {
        fun onFinish()
    }

    companion object {
        const val AD_NODE = "ad"
        const val MAIN_NODE = "main"
        const val INFO_NODE = "info"
    }

}