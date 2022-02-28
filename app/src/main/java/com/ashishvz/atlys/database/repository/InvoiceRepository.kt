package com.ashishvz.atlys.database.repository

import com.ashishvz.atlys.database.Status
import com.ashishvz.atlys.database.dao.InvoiceDao
import com.ashishvz.atlys.database.entities.Invoice
import javax.inject.Inject

class InvoiceRepository  @Inject constructor(
    private val invoiceDao: InvoiceDao
){
    fun getAllInvoices() =  invoiceDao.getAllInvoices()

    fun getInvoice(id: String) = invoiceDao.getInvoiceById(id)

    fun insert(invoice: Invoice) = invoiceDao.insert(invoice)

    fun updateInvoiceStatus(invoiceId: String, status: Status) = invoiceDao.updateInvoiceStatus(invoiceId, status)

    fun deleteInvoice(invoiceId: String) = invoiceDao.deleteInvoice(invoiceId)

}