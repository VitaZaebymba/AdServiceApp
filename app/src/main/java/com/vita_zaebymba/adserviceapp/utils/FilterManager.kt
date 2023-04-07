package com.vita_zaebymba.adserviceapp.utils

import com.vita_zaebymba.adserviceapp.model.Ad
import com.vita_zaebymba.adserviceapp.model.AdFilter

object FilterManager {
    fun createFilter(ad: Ad): AdFilter{
        return AdFilter(
            ad.time,
            "${ad.category}_${ad.time}",
            "${ad.category}_${ad.country}_${ad.delivery}_${ad.time}",
            "${ad.category}_${ad.country}_${ad.city}_${ad.delivery}_${ad.time}",
            "${ad.category}_${ad.country}_${ad.city}_${ad.index}_${ad.delivery}_${ad.time}",
            "${ad.category}_${ad.index}_${ad.delivery}_${ad.time}",
            "${ad.category}_${ad.delivery}_${ad.time}",

            "${ad.country}_${ad.delivery}_${ad.time}",
            "${ad.country}_${ad.city}_${ad.delivery}_${ad.time}",
            "${ad.country}_${ad.city}_${ad.index}_${ad.delivery}_${ad.time}",
            "${ad.index}_${ad.delivery}_${ad.time}",
            "${ad.delivery}_${ad.time}"
        )
    }

    fun getFilter(filter: String): String { // получение фильтра, передача выбранного фильтра в бд с помощью databaseManager и запрос нужного объявления, согласно выбранной фильтрации
        val sBuilder = StringBuilder()
        val tempArray = filter.split("_")
        if (tempArray[0] != "empty") sBuilder.append("country_")
        if (tempArray[1] != "empty") sBuilder.append("city_")
        if (tempArray[2] != "empty") sBuilder.append("index_")
        sBuilder.append("delivery_time")

        return sBuilder.toString()
    }
}