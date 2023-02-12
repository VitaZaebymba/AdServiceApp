package com.vita_zaebymba.adserviceapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.databinding.ListImageFragmentBinding
import com.vita_zaebymba.adserviceapp.dialoghelper.ProgressDialog
import com.vita_zaebymba.adserviceapp.utils.ImageManager
import com.vita_zaebymba.adserviceapp.utils.ImagePicker
import com.vita_zaebymba.adserviceapp.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFragment(private val fragCloseInterface: FragmentCloseInterface, private val newList: ArrayList<String>?): Fragment() { // этот фрагмент запускает список с картинками
   lateinit var rootElement: ListImageFragmentBinding

    val adapter = SelectImageRvAdapter()
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback) //класс, который будет следить за перетаскиванием элементов
    private var job: Job? = null
    private var addImageItem: MenuItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootElement = ListImageFragmentBinding.inflate(inflater)
        return rootElement.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()

        touchHelper.attachToRecyclerView(rootElement.rcViewSelectedImage)
        rootElement.rcViewSelectedImage.layoutManager = LinearLayoutManager(activity) // указываем, как элементы будут располагаться
        rootElement.rcViewSelectedImage.adapter = adapter // присваиваем адаптер

        if (newList != null){
            resizeSelectedImages(newList, true)
        }

    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>){ // обновление адаптера, если картинки уже есть
        adapter.updateAdapter(bitmapList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragmentClose(adapter.mainArray)
        job?.cancel()
    }

    private fun resizeSelectedImages(newList: ArrayList<String>, needClear: Boolean){
        job = CoroutineScope(Dispatchers.Main).launch { // создание корутины

            val dialog = ProgressDialog.createProgressDialog(activity as Activity)
            val bitmapList = ImageManager.imageResize(newList) // уменьшаем картинки и выдаем bitmapList
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear) // bitmapList Передаем в адаптер и добавляем эту картинку

            if (adapter.mainArray.size > 4) addImageItem?.isVisible = false
        }
    }


    private fun setUpToolbar(){
        rootElement.tb.inflateMenu(R.menu.menu_choose_image)
        val deleteItem = rootElement.tb.menu.findItem(R.id.delete_image)
        addImageItem = rootElement.tb.menu.findItem(R.id.id_add_image)

        rootElement.tb.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            addImageItem?.isVisible = true
            true
        }
        addImageItem?.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.getImages(activity as AppCompatActivity, imageCount, ImagePicker.REQUEST_CODE_GET_IMAGES)
            true
        }
    }

    fun updateAdapter(newList: ArrayList<String>){ // к имеющимся картинкам добавляем еще картинки
        resizeSelectedImages(newList, false)
    }


    fun setSingleImage(uri: String, position: Int){ //uri - ссылка новой картинки, на которую хоти заменить старое фото

        val pBar = rootElement.rcViewSelectedImage[position].findViewById<ProgressBar>(R.id.pBar)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(listOf(uri))
            pBar.visibility = View.GONE
            adapter.mainArray[position] = bitmapList[0]
            adapter.notifyItemChanged(position)
        }

    }

}