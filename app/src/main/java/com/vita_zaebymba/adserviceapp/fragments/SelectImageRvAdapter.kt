package com.vita_zaebymba.adserviceapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.activity.EditAdAct
import com.vita_zaebymba.adserviceapp.databinding.SelectImageFragItemBinding
import com.vita_zaebymba.adserviceapp.utils.AdapterCallback
import com.vita_zaebymba.adserviceapp.utils.ImagePicker
import com.vita_zaebymba.adserviceapp.utils.ItemTouchMoveCallback

class SelectImageRvAdapter(val adapterCallback: AdapterCallback): RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {
    val mainArray = ArrayList<Bitmap> ()//список со всеми item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {

        val viewBinding = SelectImageFragItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)  // шаблон
        return ImageHolder(viewBinding, parent.context, this)
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


    class ImageHolder(private val viewBinding: SelectImageFragItemBinding, val context: Context, val adapter: SelectImageRvAdapter) : RecyclerView.ViewHolder(viewBinding.root) {

        fun setData(bitMap: Bitmap){ //передаем ссылку и title

            viewBinding.imEditImage.setOnClickListener {

                //pBar.visibility = View.VISIBLE
                ImagePicker.getImages(context as EditAdAct, 1, ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE) // выбор картинки
                context.editImagePosition = adapterPosition
            }

            viewBinding.imDelete.setOnClickListener {
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (n in 0 until adapter.mainArray.size) { // чтобы менялся текст в соответствии с позицей фото
                    adapter.notifyItemChanged(n)
                }
                adapter.adapterCallback.onItemDelete()

            }

            viewBinding.tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            viewBinding.imageContent.setImageBitmap(bitMap)//передаем картинку

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: List<Bitmap>, needClear: Boolean){
        if (needClear) mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }


}