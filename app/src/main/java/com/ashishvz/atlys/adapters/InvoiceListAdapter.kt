package com.ashishvz.atlys.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ashishvz.atlys.R
import com.ashishvz.atlys.database.entities.Invoice
import com.ashishvz.atlys.databinding.CardInvoiceDetailBinding
import com.ashishvz.atlys.databinding.FragmentInvoiceListBinding
import com.ashishvz.atlys.fragments.InvoiceListFragmentDirections

class InvoiceListAdapter :
    ListAdapter<Invoice, InvoiceListAdapter.InvoiceViewHolder>(InvoiceDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        return InvoiceViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.card_invoice_detail,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class InvoiceViewHolder(
        private val binding: CardInvoiceDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                val direction = InvoiceListFragmentDirections.actionInvoiceListFragmentToInvoiceDetailsFragment(binding.cardInvoice?.invoiceId)
                it.findNavController().navigate(direction)
            }
        }

        fun bind(invoice: Invoice) {
            with(binding) {
                cardInvoice = invoice
                executePendingBindings()
            }
        }
    }
}

class InvoiceDiffCallBack : DiffUtil.ItemCallback<Invoice>() {
    override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
        return oldItem.invoiceId == newItem.invoiceId
    }

    override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
        return oldItem.invoiceId == newItem.invoiceId
    }

}
