package com.vita_zaebymba.adserviceapp.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.vita_zaebymba.adserviceapp.MainActivity
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.activity.DescriptionActivity
import com.vita_zaebymba.adserviceapp.activity.EditAdAct
import com.vita_zaebymba.adserviceapp.model.Ad
import com.vita_zaebymba.adserviceapp.databinding.AdListItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdRcAdapter(val act: MainActivity): RecyclerView.Adapter<AdRcAdapter.AdHolder>() {

    val adArray = ArrayList<Ad>()
    private var timeFormatter: SimpleDateFormat? = null

    init {
        timeFormatter = SimpleDateFormat("dd/MM/yy - HH:mm", Locale.getDefault())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdHolder(binding, act, timeFormatter!!) // хранит ссылки на все view
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int {
        return adArray.size
    }

    fun updateAdapter(newList: List<Ad>){
        val tempArray = ArrayList<Ad>()
        tempArray.addAll(adArray)
        tempArray.addAll(newList)

        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(adArray, tempArray)) // обновление списка, анимация
        diffResult.dispatchUpdatesTo(this) // применить обновления к адаптеру
        adArray.clear()
        adArray.addAll(tempArray)
    }

    fun updateAdapterWithClear(newList: List<Ad>){
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(adArray, newList))
        diffResult.dispatchUpdatesTo(this)
        adArray.clear()
        adArray.addAll(newList)
    }


    class AdHolder(val binding: AdListItemBinding, val act: MainActivity, val formatter: SimpleDateFormat): RecyclerView.ViewHolder(binding.root) { // переиспользуем элементы, заполняем объявлениями

        fun setData(ad: Ad) = with(binding) {
            tvTitleAdList.text = ad.title
            tvDiscription.text = ad.description
            tvPrice.text = ad.price
            tvViewCounter.text = ad.viewsCounter
            tvFavoriteCounter.text = ad.favCounter
            val publishTime = "Время публикации: ${getTimeFromMillis(ad.time!!)}"
            tvPublishTime.text = publishTime

            Picasso.get().load(ad.mainImage).into(mainImage) // показ картинки в объявлении на главной странице

            isFav(ad)
            showEditPanel(isOwner(ad))
            mainOnClick(ad)
        }

        private fun getTimeFromMillis(timeMillis: String): String { // перевод времени
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeMillis.toLong()
            return formatter.format(calendar.time)
        }


        private fun mainOnClick(ad: Ad) = with(binding){
            ibFavorite.setOnClickListener {
                if(act.mAuth.currentUser?.isAnonymous == false) act.onFavClicked(ad)
            }

            itemView.setOnClickListener { // нажатие на объявление (для счетчика просмотров), 1 шаг
                act.onAdViewed(ad)
            }

            ibEditAd.setOnClickListener(onClickEdit(ad))
            ibDeleteAd.setOnClickListener{
                act.onDeleteItem(ad) // интерфейс
            }
        }

        private fun isFav(ad: Ad){
            if (ad.isFav) {
                binding.ibFavorite.setImageResource(R.drawable.ic_fav_pressed)
            } else {
                binding.ibFavorite.setImageResource(R.drawable.ic_fav_normal)
            }
        }

        private fun onClickEdit(ad: Ad): View.OnClickListener { // редактирование объявления после публикации
            return View.OnClickListener {
                val editIntent = Intent(act, EditAdAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.ADS_DATA, ad)
                }
                act.startActivity(editIntent)
            }
        }

        private fun isOwner(ad: Ad): Boolean { // владелец аккаунта
            return ad.uid == act.mAuth.uid
        }

        private fun showEditPanel(isOwner: Boolean) {
            if (isOwner){
                binding.editPanel.visibility = View.VISIBLE
            } else {
                binding.editPanel.visibility = View.GONE
            }
        }

    }

    interface Listener { // запускается на MainActivity при нажатии на кнопку "Удалить" и потом уже запустятся все функции для удаления ad из бд и адаптера
        fun onDeleteItem(ad: Ad)
        fun onAdViewed(ad: Ad)
        fun onFavClicked(ad: Ad)
    }

}