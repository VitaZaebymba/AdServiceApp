package com.vita_zaebymba.adserviceapp.database

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vita_zaebymba.adserviceapp.data.Ad

class DatabaseManager(val readDataCallback: ReadDataCallback?) {
    val db = Firebase.database.getReference("main") // получение инстанции бд
    val auth = Firebase.auth

    fun publishAd(ad: Ad){
        if (auth.uid != null) {
            db.child(ad.key ?: "Empty").child(auth.uid!!).child("ad").setValue(ad)
        }

    }
    fun readDataFromDb(){ // читаем данные из бд
        db.addListenerForSingleValueEvent(object: ValueEventListener{


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

}
//test