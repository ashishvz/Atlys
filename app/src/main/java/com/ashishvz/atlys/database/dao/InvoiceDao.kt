package com.ashishvz.atlys.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ashishvz.atlys.database.entities.Invoice

@Dao
interface InvoiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(invoice: Invoice)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(invoice: List<Invoice>)

    @Query("Select * from invoices")
    fun getAllInvoices(): LiveData<List<Invoice>>

    @Query("Select * from invoices where invoiceId = :id")
    fun getInvoiceById(id: String): Invoice
}