package com.vita_zaebymba.adserviceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.data.Ad
import com.vita_zaebymba.adserviceapp.databinding.AdListItemBinding
import com.vita_zaebymba.adserviceapp.databinding.SignDialogBinding

class AdRcAdapter: RecyclerView.Adapter<AdRcAdapter.AdHolder>() {

    val adArray = ArrayList<Ad>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdHolder(binding) // хранит ссылки на все view
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int {
        return adArray.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: List<Ad>){
        adArray.clear()
        adArray.addAll(newList)
        notifyDataSetChanged()
    }


    class AdHolder(val binding: AdListItemBinding): RecyclerView.ViewHolder(binding.root) { // переиспользуем элементы

        fun setData(ad: Ad){
            binding.apply {
                tvDiscription.text = ad.description
                tvPrice.text = ad.price
            }
        }


    }

}