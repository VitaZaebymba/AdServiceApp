package com.vita_zaebymba.adserviceapp.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vita_zaebymba.adserviceapp.data.Ad

class DatabaseManager {
    val db = Firebase.database.getReference("main") // получение инстанции бд

    fun publishAd(ad: Ad){
        db.setValue("Hello")
    }

}