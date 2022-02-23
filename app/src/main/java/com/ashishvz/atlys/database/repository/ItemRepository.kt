package com.ashishvz.atlys.database.repository

import com.ashishvz.atlys.database.dao.ItemDao
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val itemDao: ItemDao
) {
    suspend fun getAllItemsForInvoice(id: String) = itemDao.getItemForInvoice(id)
}