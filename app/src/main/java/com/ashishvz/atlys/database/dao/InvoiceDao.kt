package com.ashishvz.atlys.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ashishvz.atlys.database.entities.Invoice

@Dao
interface InvoiceDao {

    @Insert
    fun insert(invoice: Invoice)

    @Insert
    fun insertAll(invoice: List<Invoice>)

    @Query("Select * from invoices")
    fun getAllInvoices(): List<Invoice>

    @Query("Select 1 from invoices where invoiceId = :id")
    fun getInvoiceById(id: String): Invoice
}