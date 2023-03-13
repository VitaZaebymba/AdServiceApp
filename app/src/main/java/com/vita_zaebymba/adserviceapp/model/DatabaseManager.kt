package com.vita_zaebymba.adserviceapp.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query

import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseManager {
    val db = Firebase.database.getReference("main") // получение инстанции бд
    val auth = Firebase.auth

    fun publishAd(ad: Ad, finishListener: FinishWorkListener){
        if (auth.uid != null) {
            db.child(ad.key ?: "Empty").child(auth.uid!!).child("ad").setValue(ad).addOnCompleteListener { // ожидание окончания записи в бд
                finishListener.onFinish()
            }
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



    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?){ // читаем данные из бд
        query.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val adArray = ArrayList<Ad>()
                for (item in snapshot.children) {
                    val ad = item.children.iterator().next().child("ad").getValue(Ad::class.java)
                    if (ad != null ) adArray.add(ad) // заполняем массив объявлениеями

                }
                readDataCallback?.readData(adArray)

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

}