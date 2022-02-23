package com.ashishvz.atlys.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ashishvz.atlys.database.AppDatabase
import com.ashishvz.atlys.databinding.FragmentInvoiceListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvoiceListFragment: Fragment() {
    lateinit var binding: FragmentInvoiceListBinding
    private var database: AppDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoiceListBinding.inflate(layoutInflater, container, false)
        database = AppDatabase.getInstance(requireContext())
        return binding.root
    }
}