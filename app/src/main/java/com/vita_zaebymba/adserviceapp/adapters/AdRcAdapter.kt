package com.vita_zaebymba.adserviceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.vita_zaebymba.adserviceapp.data.Ad
import com.vita_zaebymba.adserviceapp.databinding.AdListItemBinding
import com.vita_zaebymba.adserviceapp.databinding.SignDialogBinding
import java.security.acl.Owner

class AdRcAdapter(val auth: FirebaseAuth): RecyclerView.Adapter<AdRcAdapter.AdHolder>() {

    val adArray = ArrayList<Ad>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        val binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdHolder(binding, auth) // хранит ссылки на все view
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


    class AdHolder(val binding: AdListItemBinding, val auth: FirebaseAuth): RecyclerView.ViewHolder(binding.root) { // переиспользуем элементы, заполняем объявлениями

        fun setData(ad: Ad){
            binding.apply {
                tvTitleAdList.text = ad.title
                tvDiscription.text = ad.description
                tvPrice.text = ad.price
            }
            showEditPanel(isOwner(ad))
        }

        private fun isOwner(ad: Ad): Boolean { // владелец аккаунта
            return ad.uid == auth.uid
        }

        private fun showEditPanel(isOwner: Boolean) {
            if (isOwner){
                binding.editPanel.visibility = View.VISIBLE
            } else {
                binding.editPanel.visibility = View.GONE
            }
        }


    }

}