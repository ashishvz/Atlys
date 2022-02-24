package com.ashishvz.atlys.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ashishvz.atlys.adapters.ItemListAdapter
import com.ashishvz.atlys.databinding.FragmentInvoiceDetailBinding
import com.ashishvz.atlys.viewmodels.InvoiceViewModel
import com.ashishvz.atlys.viewmodels.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InvoiceDetailsFragment : Fragment() {
    lateinit var binding: FragmentInvoiceDetailBinding
    private val itemViewModel: ItemViewModel by viewModels()
    private val invoiceViewModel:InvoiceViewModel by viewModels()
    private val masterViewModel: MasterViewModel by viewModels()
    private val args: InvoiceDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoiceDetailBinding.inflate(layoutInflater, container, false)
        val adapter = ItemListAdapter()
        binding.buttonBackInvoiceDetail.setOnClickListener {
            findNavController().navigate(InvoiceDetailsFragmentDirections.actionInvoiceDetailsFragmentToInvoiceListFragment())
        }
        binding.itemsRecyclerView.adapter = adapter
        subscribeUI(adapter)
        return binding.root
    }

    private fun subscribeUI(itemListAdapter: ItemListAdapter) {
        lifecycleScope.launch {
            binding.cardInvoice = invoiceViewModel.getInvoice(args.invoiceId!!)
        }
        itemViewModel.getItemForInvoice(args.invoiceId!!).observe(viewLifecycleOwner) {
            itemListAdapter.submitList(it)
        }
    }
}