package com.ashishvz.atlys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ashishvz.atlys.database.Status
import com.ashishvz.atlys.database.entities.Invoice
import com.ashishvz.atlys.database.repository.InvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val repository: InvoiceRepository
): ViewModel() {
    val invoiceData: LiveData<List<Invoice>> = repository.getAllInvoices()

    fun invoices(isStatus: Boolean, status: Status?): LiveData<List<Invoice>> {
        return if (isStatus && status != null) {
            repository.getInvoicesByStatus(status)
        } else {
            repository.getAllInvoices()
        }
    }

    fun getInvoice(invoiceId: String): Invoice {
        return repository.getInvoice(invoiceId)
    }

    fun getInvoiceByStatus(status: Status) = repository.getInvoicesByStatus(status)

    fun insertInvoice(invoice: Invoice) = repository.insert(invoice)

    fun updateInvoiceStatus(invoiceId: String, status: Status) = repository.updateInvoiceStatus(invoiceId, status)

    fun deleteInvoice(invoiceId: String) = repository.deleteInvoice(invoiceId)
}