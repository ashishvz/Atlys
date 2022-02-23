package com.ashishvz.atlys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ashishvz.atlys.database.entities.Invoice
import com.ashishvz.atlys.database.repository.InvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class InvoiceViewModel @Inject constructor(
    repository: InvoiceRepository
): ViewModel() {
    val invoiceData: LiveData<List<Invoice>> = repository.getAllInvoices()

}