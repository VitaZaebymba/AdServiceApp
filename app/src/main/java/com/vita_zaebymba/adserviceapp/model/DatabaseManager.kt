package com.vita_zaebymba.adserviceapp.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query

import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.vita_zaebymba.adserviceapp.utils.FilterManager

class DatabaseManager {
    val db = Firebase.database.getReference(MAIN_NODE) // получение инстанции бд
    val dbStorage = Firebase.storage.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishAd(ad: Ad, finishListener: FinishWorkListener) {
        if (auth.uid != null) db.child(ad.key ?: "Empty").child(auth.uid!!).child(AD_NODE)
            .setValue(ad).addOnCompleteListener { // ожидание окончания записи в бд

            val adFilter = FilterManager.createFilter(ad)
            db.child(ad.key ?: "Empty").child(FILTER_NODE).setValue(adFilter)
                .addOnCompleteListener {
                    finishListener.onFinish() // (it.isSuccessful)
                }
        }
    }

    fun adViewed(ad: Ad){ // объявляение просмотрено, 4 шаг
        var counter = ad.viewsCounter!!.toInt()
        counter++
        if (auth.uid != null) {
            db.child(ad.key ?: "Empty")
                .child(INFO_NODE)
                .setValue(InfoItem(counter.toString(), ad.emailCounter, ad.callsCounter))
        }
    }

    fun onFavClick(ad: Ad, listener: FinishWorkListener){ // определяет, какую функцию запустить: addToFavs или removeFromFavs
        if (ad.isFav) {
            removeFromFavs(ad, listener)
        } else {
            addToFavs(ad, listener)
        }
    }

    private fun addToFavs(ad: Ad, listener: FinishWorkListener){ // добавление в избранное
        ad.key?.let {
            auth.uid?.let { uid -> db.child(it).child(FAVS_NODE).child(uid).setValue(uid).addOnCompleteListener { // db - основной путь, it - key, далее - избранное (favs)
                if(it.isSuccessful) listener.onFinish() // (true)
            } }
        }
    }

    private fun removeFromFavs(ad: Ad, listener: FinishWorkListener){ // удаление из избранного
        ad.key?.let {
            auth.uid?.let { uid -> db.child(it).child(FAVS_NODE).child(uid).removeValue().addOnCompleteListener { // db - основной путь, it - key, далее - избранное (favs)
                if(it.isSuccessful) listener.onFinish()
            } }
        }
    }


    fun getMyAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid) // выдать все объявления, принадлежащие uid
        readDataFromDb(query, readDataCallback)
    }

    fun getMyFavs(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild( "/favs/${auth.uid}").equalTo(auth.uid) // выдать все объявления, принадлежащие uid
        readDataFromDb(query, readDataCallback)
    }



    /******** ИСПОЛЬЗОВАНИЕ ФИЛЬТРА - ПЛЯСКИ С БУБНОМ (НАЧАЛО) ********/

    fun getAllAdsFirstPage(filter: String, readDataCallback: ReadDataCallback?){
        val query = if (filter.isEmpty()){
            db.orderByChild(ADFILTER_TIME).limitToLast(ADS_LIMIT)
        } else {
            getAllAdsByFilterFirstPage(filter)
        }
        readDataFromDb(query, readDataCallback)
    }

    fun getAllAdsByFilterFirstPage(tempFilter: String): Query { // фильтрация первой страницы
        val orderBy = tempFilter.split("|")[0]
        val filter = tempFilter.split("|")[1]
        return db.orderByChild("/adFilter/$orderBy")
            .startAt(filter).endAt(filter + "\uf8ff").limitToLast(ADS_LIMIT) // если неизвестно время, то берем uf8ff

    }

    fun getAllAdsNextPage(time: String, filter: String, readDataCallback: ReadDataCallback?){
        if (filter.isEmpty()){
            val query = db.orderByChild(ADFILTER_TIME).endBefore(time).limitToLast(ADS_LIMIT)
            readDataFromDb(query, readDataCallback)
        } else {
            getAllAdsByFilterNextPage(filter, time, readDataCallback)
        }

    }

    private fun getAllAdsByFilterNextPage(tempFilter: String, time: String, readDataCallback: ReadDataCallback?){
        val orderBy = tempFilter.split("|")[0]
        val filter = tempFilter.split("|")[1]
        val query = db.orderByChild("/adFilter/$orderBy")
            .endBefore(filter + "_$time").limitToLast(ADS_LIMIT)
        readNextPageFromDb(query, filter, orderBy, readDataCallback) // если объявление по фильтру не совпадает, оно отсеется
    }

    fun getAllAdsFromCatFirstPage(cat: String, filter: String, readDataCallback: ReadDataCallback?){
        val query = if(filter.isEmpty()){
            db.orderByChild(ADFILTER_CAT_TIME)
                .startAt(cat).endAt(cat + "_\uf8ff")
                .limitToLast(ADS_LIMIT) // если неизвестно время, то берем uf8ff
        } else {
            getAllAdsFromCatByFilterFirstPage(cat, filter)
        }
        readDataFromDb(query, readDataCallback)
    }

    fun getAllAdsFromCatByFilterFirstPage(cat: String, tempFilter: String): Query { // фильтрация первой страницы по категориям
        val orderBy = "cat_" + tempFilter.split("|")[0]
        val filter = cat + "_" + tempFilter.split("|")[1]
        return db.orderByChild("/adFilter/$orderBy")
            .startAt(filter).endAt(filter + "\uf8ff").limitToLast(ADS_LIMIT) // если неизвестно время, то берем uf8ff
    }

    fun getAllAdsFromCatNextPage(cat: String, time: String, filter: String, readDataCallback: ReadDataCallback?){
        if (filter.isEmpty()){
            val query = db.orderByChild(ADFILTER_CAT_TIME).endBefore(cat + "_" + time).limitToLast(ADS_LIMIT)
            readDataFromDb(query, readDataCallback)
        } else {
            getAllAdsFromCatByFilterNextPage(cat, time, filter, readDataCallback)
        }
    }

    fun getAllAdsFromCatByFilterNextPage(cat: String, time: String, tempFilter: String, readDataCallback: ReadDataCallback?) {
        val orderBy = "cat_" + tempFilter.split("|")[0]
        val filter = cat + "_" + tempFilter.split("|")[1]
        val query = db.orderByChild("/adFilter/$orderBy")
            .endBefore(filter + "_" + time).limitToLast(ADS_LIMIT)
        readNextPageFromDb(query, filter, orderBy, readDataCallback)
    }


    /******** ИСПОЛЬЗОВАНИЕ ФИЛЬТРА - ПЛЯСКИ С БУБНОМ (КОНЕЦ) ********/




    fun  deleteAd(ad: Ad, listener: FinishWorkListener) {
        if (ad.key == null || ad.uid == null) return
        val map = mapOf(
            "/adFilter" to null,
            "/info" to null,
            "/favs" to null,
            "/${ad.uid}" to null
        )
        db.child(ad.key).updateChildren(map).addOnCompleteListener { // ключ объявления - название узла, где находится объявление, операция асинхронная
            if (it.isSuccessful) deleteImagesFromStorage(ad, 0, listener)
        }
    }

    private fun deleteImagesFromStorage(ad: Ad, index: Int, listener: FinishWorkListener) {
        val imageList = listOf(ad.mainImage, ad.image2, ad.image3)
        dbStorage.storage.getReferenceFromUrl(imageList[index]!!).delete().addOnCompleteListener { // ключ объявления - название узла, где находится объявление, операция асинхронная
            if (it.isSuccessful) { // (true)
                if (imageList.size > index + 1) {
                    if (imageList[index + 1] != "empty") {
                        deleteImagesFromStorage(ad, index + 1, listener)
                    } else {
                        listener.onFinish()
                    }
                } else {
                    listener.onFinish()
                }
            }
        }
    }



    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?){ // читаем данные из бд
        query.addListenerForSingleValueEvent(object: ValueEventListener{ // query - путь, откуда считываем данные

            override fun onDataChange(snapshot: DataSnapshot) { // в snapshot находятся считанные данные
                val adArray = ArrayList<Ad>()
                for (item in snapshot.children) {

                    var ad: Ad? = null

                    item.children.forEach{ // цикл внутри узла key
                        if (ad == null) ad = it.child(AD_NODE).getValue(Ad::class.java)
                    }
                    val infoItem = item.child(INFO_NODE).getValue(InfoItem::class.java)

                    val favCounter = item.child(FAVS_NODE).childrenCount // кол-во элементов, которые находятся на этом пути
                    val isFav = auth.uid?.let { item.child(FAVS_NODE).child(it).getValue(String::class.java) }
                    ad?.isFav = isFav != null // узнаем, объявление в избранном или нет

                    ad?.favCounter = favCounter.toString()

                    // перезагружаем данные в объявление
                    ad?.viewsCounter = infoItem?.viewsCounter ?: "0"
                    ad?.emailCounter = infoItem?.emailCounter ?: "0"
                    ad?.callsCounter = infoItem?.callsCounter ?: "0"
                    if (ad != null ) adArray.add(ad!!) // заполняем массив объявлениеями

                }
                readDataCallback?.readData(adArray) // отправляем список на адаптер

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    private fun readNextPageFromDb(query: Query, filter: String, orderBy: String, readDataCallback: ReadDataCallback?) {
        query.addListenerForSingleValueEvent(object: ValueEventListener{ // query - путь, откуда считываем данные

            override fun onDataChange(snapshot: DataSnapshot) { // в snapshot находятся считанные данные
                val adArray = ArrayList<Ad>()
                for (item in snapshot.children) {

                    var ad: Ad? = null

                    item.children.forEach{ // цикл внутри узла key
                        if (ad == null) ad = it.child(AD_NODE).getValue(Ad::class.java)
                    }
                    val infoItem = item.child(INFO_NODE).getValue(InfoItem::class.java)
                    val filterNotValue = item.child(FILTER_NODE).child(orderBy).value.toString()

                    val favCounter = item.child(FAVS_NODE).childrenCount // кол-во элементов, которые находятся на этом пути
                    val isFav = auth.uid?.let { item.child(FAVS_NODE).child(it).getValue(String::class.java) }
                    ad?.isFav = isFav != null // узнаем, объявление в избранном или нет

                    ad?.favCounter = favCounter.toString()

                    // перезагружаем данные в объявление
                    ad?.viewsCounter = infoItem?.viewsCounter ?: "0"
                    ad?.emailCounter = infoItem?.emailCounter ?: "0"
                    ad?.callsCounter = infoItem?.callsCounter ?: "0"
                    if (ad != null && filterNotValue.startsWith(filter)) adArray.add(ad!!) // заполняем массив объявлениеями

                }
                readDataCallback?.readData(adArray) // отправляем список на адаптер

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    interface ReadDataCallback {
        fun readData(list: ArrayList<Ad>)
    }

    interface FinishWorkListener { // чтобы знать, когда запись прошла
        fun onFinish() // (isDone: Boolean)
    }

    companion object {
        const val AD_NODE = "ad"
        const val FILTER_NODE = "adFilter"
        const val MAIN_NODE = "main"
        const val INFO_NODE = "info"
        const val FAVS_NODE = "favs"
        const val ADS_LIMIT = 30

        const val ADFILTER_TIME = "/adFilter/time"
        const val ADFILTER_CAT_TIME = "/adFilter/cat_time"
    }

}