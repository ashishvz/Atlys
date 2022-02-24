package com.ashishvz.atlys.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ashishvz.atlys.R
import com.ashishvz.atlys.adapters.InvoiceListAdapter
import com.ashishvz.atlys.database.AppDatabase
import com.ashishvz.atlys.databinding.FragmentInvoiceListBinding
import com.ashishvz.atlys.viewmodels.InvoiceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvoiceListFragment: Fragment() {
    lateinit var binding: FragmentInvoiceListBinding
    private val viewModel: InvoiceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoiceListBinding.inflate(layoutInflater, container, false)
        val adapter = InvoiceListAdapter()
        binding.invoicesRecyclerView.adapter = adapter
        subscribeUI(adapter)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeUI(adapter: InvoiceListAdapter) {
        viewModel.invoiceData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.noDataLayout.visibility = View.GONE
                binding.invoicesRecyclerView.visibility = View.VISIBLE
                binding.invoiceCount.text = it.size.toString() + " " + getString(R.string.invoices)
                adapter.submitList(it)
            } else {
                binding.noDataLayout.visibility = View.VISIBLE
                binding.invoicesRecyclerView.visibility = View.GONE
                binding.invoiceCount.text = getString(R.string.no_invoices)
            }

        }
    }
}