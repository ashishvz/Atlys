package com.ashishvz.atlys.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ashishvz.atlys.database.dao.ItemDao
import com.ashishvz.atlys.database.entities.Item
import com.ashishvz.atlys.database.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {
    fun getItemForInvoice(invoiceId: String): LiveData<List<Item>> =
        itemRepository.getAllItemsForInvoice(invoiceId)

    fun insertAllItems(items: List<Item>) = itemRepository.insertAllItems(items)

    fun deleteItemsForInvoice(invoiceId: String) = itemRepository.deleteItemsForInvoice(invoiceId)
}