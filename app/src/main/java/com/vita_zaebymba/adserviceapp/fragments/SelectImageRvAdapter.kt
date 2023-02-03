package com.vita_zaebymba.adserviceapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.utils.ItemTouchMoveCallback

class SelectImageRvAdapter: RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<String> ()//список со всеми item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_frag_item, parent, false) // шаблон
        return ImageHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) {
       val targetItem = mainArray[targetPos]
       mainArray[targetPos] = mainArray[startPos]
       mainArray[startPos] = targetItem
       notifyItemMoved(startPos, targetPos)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClear() {
        notifyDataSetChanged()
    }


    class ImageHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvTitle: TextView
        lateinit var image: ImageView

        fun setData(item: String){ //передаем ссылку и title
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageContent)
            tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            image.setImageURI(Uri.parse(item))//передаем картинку

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: List<String>, needClear: Boolean){
        if (needClear) mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }


}