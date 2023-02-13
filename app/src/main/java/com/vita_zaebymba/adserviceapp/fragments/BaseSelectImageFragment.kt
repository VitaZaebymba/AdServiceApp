package com.vita_zaebymba.adserviceapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vita_zaebymba.adserviceapp.databinding.ListImageFragmentBinding

open class BaseSelectImageFragment: Fragment() {
    lateinit var binding: ListImageFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ListImageFragmentBinding.inflate(layoutInflater)
        return binding.root
    }
}