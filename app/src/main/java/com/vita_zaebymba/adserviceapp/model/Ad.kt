package com.vita_zaebymba.adserviceapp.model

import java.io.Serializable

data class Ad( // бд поместит сразу все значения
    val title: String? = null,
    val country: String? = null,
    val city: String? = null,
    val tel: String? = null,
    val index: String? = null,
    val delivery: String? = null,
    val category: String? = null,
    val price: String? = null,
    val description: String? = null,
    val key: String? = null,
    val uid: String? = null,
    val isFav: Boolean = false,

    // 3 переменные для записи инфы из InfoItem
    var viewsCounter: String? = "0",
    var emailCounter: String? = "0",
    var callsCounter: String? = "0"
) : Serializable