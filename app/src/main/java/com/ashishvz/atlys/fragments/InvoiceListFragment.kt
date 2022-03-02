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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ashishvz.atlys.R
import com.ashishvz.atlys.adapters.InvoiceListAdapter
import com.ashishvz.atlys.database.AppDatabase
import com.ashishvz.atlys.database.Status
import com.ashishvz.atlys.databinding.FragmentInvoiceListBinding
import com.ashishvz.atlys.utils.PAYMENT_TERMS_LIST
import com.ashishvz.atlys.viewmodels.InvoiceViewModel
import com.ashishvz.atlys.viewmodels.MasterViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvoiceListFragment : Fragment() {
    lateinit var binding: FragmentInvoiceListBinding
    private val viewModel: InvoiceViewModel by viewModels()
    private val masterViewModel: MasterViewModel by viewModels()
    private var adapter = InvoiceListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoiceListBinding.inflate(layoutInflater, container, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        masterViewModel.isLoading.postValue(true)
        binding.newButton.setOnClickListener {
            findNavController().navigate(InvoiceListFragmentDirections.actionInvoiceListFragmentToNewInvoiceFragment())
        }
        binding.filterLayout.setOnClickListener {
            filterDialog()
        }
        adapter = InvoiceListAdapter()
        binding.invoicesRecyclerView.adapter = adapter
        subscribeUI(adapter, false, null)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeUI(adapter: InvoiceListAdapter, isStatus: Boolean, status: Status?) {
        viewModel.invoices(isStatus, status).observe(viewLifecycleOwner) {
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
            masterViewModel.isLoading.postValue(false)
        }
        masterViewModel.isLoading.observe(viewLifecycleOwner) {
            if (!it) {
                binding.progressLoadingLayout.visibility = View.GONE
            } else {
                binding.progressLoadingLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun filterDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_filter)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val filterListView = dialog.findViewById<ListView>(R.id.filterList)
        val clearFilterBtn = dialog.findViewById<MaterialButton>(R.id.clearFilter)
        val cancelBtn = dialog.findViewById<MaterialButton>(R.id.cancelButton)
        val statusListAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_textview_layout, Status.values())
        filterListView.adapter = statusListAdapter
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        filterListView.setOnItemClickListener { adapterView, _, i, _ ->
            subscribeUI(
                adapter,
                true,
                Status.valueOf(adapterView!!.getItemAtPosition(i).toString())
            )
            binding.filterText.text = adapterView.getItemAtPosition(i).toString()
            dialog.dismiss()
        }
        clearFilterBtn.setOnClickListener {
            subscribeUI(adapter, false, null)
            binding.filterText.text = requireContext().resources.getString(R.string.filter)
            dialog.dismiss()
        }
        dialog.show()
    }
}