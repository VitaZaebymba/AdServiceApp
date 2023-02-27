package com.vita_zaebymba.adserviceapp.fragments

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.vita_zaebymba.adserviceapp.R
import com.vita_zaebymba.adserviceapp.databinding.ListImageFragmentBinding
import com.vita_zaebymba.adserviceapp.dialoghelper.ProgressDialog
import com.vita_zaebymba.adserviceapp.utils.AdapterCallback
import com.vita_zaebymba.adserviceapp.utils.ImageManager
import com.vita_zaebymba.adserviceapp.utils.ImagePicker
import com.vita_zaebymba.adserviceapp.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFragment(private val fragCloseInterface: FragmentCloseInterface, private val newList: ArrayList<String>?): BaseAdsFragment(), AdapterCallback{ // этот фрагмент запускает список с картинками

    val adapter = SelectImageRvAdapter(this)
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback) //класс, который будет следить за перетаскиванием элементов
    private var job: Job? = null
    private var addImageItem: MenuItem? = null
    lateinit var binding: ListImageFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ListImageFragmentBinding.inflate(layoutInflater)
        adView = binding.adView
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        binding.apply{
            touchHelper.attachToRecyclerView(rcViewSelectedImage)
            rcViewSelectedImage.layoutManager = LinearLayoutManager(activity) // указываем, как элементы будут располагаться
            rcViewSelectedImage.adapter = adapter // присваиваем адаптер

            if (newList != null) {
                resizeSelectedImages(newList, true)
            }
        }

    }

    override fun onItemDelete() {
        addImageItem?.isVisible = true
    }



    fun updateAdapterFromEdit(bitmapList: List<Bitmap>){ // обновление адаптера, если картинки уже есть
        adapter.updateAdapter(bitmapList, true)
    }

    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragmentClose(adapter.mainArray)
        job?.cancel()
    }

    override fun onClose() {
        super.onClose()
        activity?.supportFragmentManager?.beginTransaction()?.remove(this@ImageListFragment)?.commit() // удаляем фрагмент, остается активити
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

        binding.apply {
            tb.inflateMenu(R.menu.menu_choose_image)
            val deleteItem = tb.menu.findItem(R.id.delete_image)
            addImageItem = tb.menu.findItem(R.id.id_add_image)

            tb.setNavigationOnClickListener {
                showInterAd()
            }

            deleteItem.setOnMenuItemClickListener {
                adapter.updateAdapter(ArrayList(), true)
                addImageItem?.isVisible = true
                true
            }
            addImageItem?.setOnMenuItemClickListener {
                val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
                /*ImagePicker.getImages(
                    activity as AppCompatActivity,
                    imageCount,
                    ImagePicker.REQUEST_CODE_GET_IMAGES
                )*/
                true
            }
        }
    }

    fun updateAdapter(newList: ArrayList<String>){ // к имеющимся картинкам добавляем еще картинки
        resizeSelectedImages(newList, false)
    }


    fun setSingleImage(uri: String, position: Int){ //uri - ссылка новой картинки, на которую хоти заменить старое фото

        val pBar = binding.rcViewSelectedImage[position].findViewById<ProgressBar>(R.id.pBar)
        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(listOf(uri))
            pBar.visibility = View.GONE
            adapter.mainArray[position] = bitmapList[0]
            adapter.notifyItemChanged(position)
        }

    }



}