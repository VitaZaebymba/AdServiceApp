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
            "${ad.delivery}_${ad.time}",


        )
    }
}