package com.ashishvz.atlys.database.repository

import com.ashishvz.atlys.database.dao.InvoiceDao
import javax.inject.Inject

class InvoiceRepository  @Inject constructor(
    private val invoiceDao: InvoiceDao
){
    fun getAllInvoices() =  invoiceDao.getAllInvoices()

    suspend fun getInvoice(id: String) = invoiceDao.getInvoiceById(id)

}