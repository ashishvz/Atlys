package com.ashishvz.atlys.database.repository

import com.ashishvz.atlys.database.dao.ItemDao
import com.ashishvz.atlys.database.entities.Item
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemDao: ItemDao
) {
    fun getAllItemsForInvoice(id: String) = itemDao.getItemForInvoice(id)

    fun insertAllItems(items: List<Item>) = itemDao.insertAll(items)

    fun deleteItemsForInvoice(invoiceId: String) = itemDao.deleteItemsForInvoice(invoiceId)
}