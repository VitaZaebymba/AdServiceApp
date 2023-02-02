package com.vita_zaebymba.adserviceapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.databinding.ListImageFragmentBinding
import com.vita_zaebymba.adserviceapp.utils.ItemTouchMoveCallback

class ImageListFragment(private val fragCloseInterface: FragmentCloseInterface, private val newList: ArrayList<String>): Fragment() { // этот фрагмент запускает список с картинками
   lateinit var rootElement: ListImageFragmentBinding

    val adapter = SelectImageRvAdapter()
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback) //класс, который будет следить за перетаскиванием элементов

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootElement = ListImageFragmentBinding.inflate(inflater)
        return rootElement.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        touchHelper.attachToRecyclerView(rootElement.rcViewSelectedImage)
        rootElement.rcViewSelectedImage.layoutManager = LinearLayoutManager(activity) // указываем, как элементы будут располагаться
        rootElement.rcViewSelectedImage.adapter = adapter // присваиваем адаптер
        val updateList = ArrayList<SelectImageItem>()
        for (n in 0 until newList.size){
            updateList.add(SelectImageItem(n.toString(), newList[n]))
        }

        adapter.updateAdapter(updateList)
            //activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()

    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragmentClose(adapter.mainArray)
    }

}