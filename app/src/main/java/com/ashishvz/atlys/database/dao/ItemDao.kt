package com.ashishvz.atlys.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ashishvz.atlys.database.entities.Item

@Dao
interface ItemDao {

    @Insert
    fun insert(items: Item)

    @Insert
    fun insertAll(items: List<Item>)

    @Query("Select * from Items where invoiceId = :id")
    fun getItemForInvoice(id: String): List<Item>
}