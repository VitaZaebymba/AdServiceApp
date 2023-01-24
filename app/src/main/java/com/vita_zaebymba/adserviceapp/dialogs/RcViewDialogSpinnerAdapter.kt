package com.vita_zaebymba.adserviceapp.dialogs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.R

class RcViewDialogSpinnerAdapter: RecyclerView.Adapter<RcViewDialogSpinnerAdapter.SpViewHolder>() {
   private val mainList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpViewHolder { //рисуем элемент
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false)
        return SpViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpViewHolder, position: Int) { //к элементу подключаем текст и т.д.
        holder.setData(mainList[position])
    }

    override fun getItemCount(): Int { //узнаем, сколько элементов рисовать
        return mainList.size
    }

    class SpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //viewHolder столько, сколько элементов
        fun setData(text: String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.tvSpItem) //хранит ссылку на элемент
            tvSpItem.text = text
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: ArrayList<String>){ //обновление списка
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()

    }
}