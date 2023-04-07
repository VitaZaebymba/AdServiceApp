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
        val sBuilderNode = StringBuilder()
        val sBuilderFilter = StringBuilder()

        val tempArray = filter.split("_")
        if (tempArray[0] != "empty") {
            sBuilderNode.append("country_")
            sBuilderFilter.append("${tempArray[0]}_")
        }
        if (tempArray[1] != "empty") {
            sBuilderNode.append("city_")
            sBuilderFilter.append("${tempArray[1]}_")
        }
        if (tempArray[2] != "empty") {
            sBuilderNode.append("index_")
            sBuilderFilter.append("${tempArray[2]}_")
        }
        sBuilderFilter.append(tempArray[3])
        sBuilderNode.append("delivery_time")

        return "$sBuilderNode|$sBuilderFilter" // получение узла и фильтра
    }
}