package com.vita_zaebymba.adserviceapp.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchMoveCallback(val adapter: ItemTouchAdapter): ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN // направление перетаскивания
        return makeMovementFlags(dragFlag, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean { //viewHolder - элемент, который взяли, target - элемент, над которым мы сейчас
        adapter.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }


    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) { // добавим прозрачность элементу
        super.onSelectedChanged(viewHolder, actionState)
    }

    interface ItemTouchAdapter{
        fun onMove(startPos: Int, targetPos: Int)
    }
}