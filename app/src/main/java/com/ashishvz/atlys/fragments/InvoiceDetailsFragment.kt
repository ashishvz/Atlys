package com.ashishvz.atlys.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ashishvz.atlys.R
import com.ashishvz.atlys.adapters.ItemListAdapter
import com.ashishvz.atlys.database.Status
import com.ashishvz.atlys.databinding.FragmentInvoiceDetailBinding
import com.ashishvz.atlys.utils.doToast
import com.ashishvz.atlys.viewmodels.InvoiceViewModel
import com.ashishvz.atlys.viewmodels.ItemViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InvoiceDetailsFragment : Fragment() {
    lateinit var binding: FragmentInvoiceDetailBinding
    private val itemViewModel: ItemViewModel by viewModels()
    private val invoiceViewModel: InvoiceViewModel by viewModels()
    private val args: InvoiceDetailsFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
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
        binding.markAsPaidButton.setOnClickListener {
            lifecycleScope.launch {
                invoiceViewModel.updateInvoiceStatus(args.invoiceId!!, Status.PAID)
            }
            doToast(requireContext(), "Invoice status changed to PAID")
            binding.buttonBackInvoiceDetail.performClick()
        }
        binding.deleteButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.delete_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val deleteDesc = dialog.findViewById<MaterialTextView>(R.id.deleteDescription)
            val cancelBtn = dialog.findViewById<MaterialButton>(R.id.cancelButton)
            val deleteBtn = dialog.findViewById<MaterialButton>(R.id.deleteButton)
            deleteDesc.text =
                "Are you sure you want to delete invoice #${args.invoiceId!!}? This action cannot be undone."
            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
            deleteBtn.setOnClickListener {
                lifecycleScope.launch {
                    invoiceViewModel.deleteInvoice(args.invoiceId!!)
                    itemViewModel.deleteItemsForInvoice(args.invoiceId!!)
                }
                dialog.dismiss()
                doToast(requireContext(), "Invoice deleted with its Items.")
                binding.buttonBackInvoiceDetail.performClick()
            }
            dialog.show()
        }
        binding.editButton.setOnClickListener {
            findNavController().navigate(InvoiceDetailsFragmentDirections.actionInvoiceDetailsFragmentToEditInvoiceFragment(args.invoiceId!!))
        }
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