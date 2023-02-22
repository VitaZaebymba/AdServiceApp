package com.vita_zaebymba.adserviceapp.database

import com.vita_zaebymba.adserviceapp.data.Ad

interface ReadDataCallback {
    fun readData(list: List<Ad>)
}