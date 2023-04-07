package com.vita_zaebymba.adserviceapp.model

data class AdFilter(
    val time: String? = null,
    val cat_time: String? = null,

    val cat_country_delivery_time: String? = null,
    val cat_country_city_delivery_time: String? = null,
    val cat_country_city_index_delivery_time: String? = null,
    val cat_index_delivery_time: String? = null,
    val cat_delivery_time: String? = null,

    val country_delivery_time: String? = null,
    val country_city_delivery_time: String? = null,
    val country_city_index_delivery_time: String? = null,
    val index_delivery_time: String? = null,
    val delivery_time: String? = null
)
