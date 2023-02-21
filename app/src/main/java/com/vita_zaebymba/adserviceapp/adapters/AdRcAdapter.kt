package com.vita_zaebymba.adserviceapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.data.Ad
import com.vita_zaebymba.adserviceapp.databinding.AdListItemBinding
import com.vita_zaebymba.adserviceapp.databinding.SignDialogBinding

class AdRcAdapter: RecyclerView.Adapter<AdRcAdapter.AdHolder>() {

    val adArray = ArrayList<Ad>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context))
        return AdHolder(binding) // хранит ссылки на все view
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
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