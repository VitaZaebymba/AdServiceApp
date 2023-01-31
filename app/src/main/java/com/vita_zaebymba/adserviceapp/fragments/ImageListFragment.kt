package com.vita_zaebymba.adserviceapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.R

class ImageListFragment(private val fragCloseInterface: FragmentCloseInterface, private val newList: ArrayList<String>): Fragment() {
    val adapter = SelectImageRvAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_image_fragment, container, false) // draw fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bBack = view.findViewById<Button>(R.id.bBack)
        val rcView = view.findViewById<RecyclerView>(R.id.rcViewSelectedImage)
        rcView.layoutManager = LinearLayoutManager(activity) // указываем, как элементы будут располагаться
        rcView.adapter = adapter // присваиваем адаптер
        val updateList = ArrayList<SelectImageItem>()
        for (n in 0 until newList.size){
            updateList.add(SelectImageItem(n.toString(), newList[n]))
        }

        adapter.updateAdapter(updateList)
        bBack.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragmentClose()
    }

}