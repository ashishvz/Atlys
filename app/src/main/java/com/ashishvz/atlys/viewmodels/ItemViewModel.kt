package com.ashishvz.atlys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ashishvz.atlys.database.dao.ItemDao
import com.ashishvz.atlys.database.entities.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemDao: ItemDao
): ViewModel() {
    fun getItemForInvoice(invoiceId: String): LiveData<List<Item>> {
        return itemDao.getItemForInvoice(invoiceId)
    }
}