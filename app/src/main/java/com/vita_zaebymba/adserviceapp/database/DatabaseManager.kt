package com.vita_zaebymba.adserviceapp.database

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DatabaseManager {
    val db = Firebase.database.reference // получение инстанции бд

    fun publishAd(){
        db.setValue("Hello")
    }

}