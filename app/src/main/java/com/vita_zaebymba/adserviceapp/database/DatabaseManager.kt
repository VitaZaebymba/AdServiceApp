package com.vita_zaebymba.adserviceapp.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vita_zaebymba.adserviceapp.data.Ad

class DatabaseManager {
    val db = Firebase.database.getReference("main") // получение инстанции бд
    val auth = Firebase.auth

    fun publishAd(ad: Ad){
        if (auth.uid != null) {
            db.child(ad.key ?: "Empty").child(auth.uid!!).child("ad").setValue(ad)
        }

    }

}