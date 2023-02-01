package com.vita_zaebymba.adserviceapp.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.fragments.SelectImageItem

class ImageAdapter: RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    private val mainArray = ArrayList<SelectImageItem>() // список с картинками


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_adapter_item, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position].imageUri) // из массива достаем элементы и заполняем holder
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }


    class ImageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var imItem: ImageView

        fun setData(uri: String){
            imItem = itemView.findViewById(R.id.imItem)
            imItem.setImageURI(Uri.parse(uri))
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: ArrayList<SelectImageItem>){
        mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }
}