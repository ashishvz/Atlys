package com.ashishvz.atlys.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ashishvz.atlys.database.dao.InvoiceDao
import com.ashishvz.atlys.database.dao.ItemDao
import javax.inject.Inject

class MasterViewModel @Inject constructor(
    invoiceDao: InvoiceDao,
    itemDao: ItemDao
): ViewModel() {
    var selectedInvoice = MutableLiveData<String>()
}