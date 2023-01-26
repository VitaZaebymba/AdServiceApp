package com.vita_zaebymba.adserviceapp.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.activity.EditAdAct

class RcViewDialogSpinnerAdapter(var tvSelection: TextView, var dialog: android.app.AlertDialog): RecyclerView.Adapter<RcViewDialogSpinnerAdapter.SpViewHolder>() {
   private val mainList = ArrayList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpViewHolder { //рисуем элемент
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false)
        return SpViewHolder(view, tvSelection, dialog)
    }

    override fun onBindViewHolder(holder: SpViewHolder, position: Int) { //к элементу подключаем текст и т.д.
        holder.setData(mainList[position])
    }

    override fun getItemCount(): Int { //узнаем, сколько элементов рисовать
        return mainList.size
    }

    class SpViewHolder(itemView: View, var tvSelection: TextView, var dialog: android.app.AlertDialog) : RecyclerView.ViewHolder(itemView), View.OnClickListener { //viewHolder столько, сколько элементов
        private var itemText = ""

        fun setData(text: String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.tvSpItem) //хранит ссылку на элемент
            tvSpItem.text = text
            itemText = text
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            tvSelection.text = itemText // выбор страны
            dialog.dismiss()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(list: ArrayList<String>){ //обновление списка
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()

    }
}