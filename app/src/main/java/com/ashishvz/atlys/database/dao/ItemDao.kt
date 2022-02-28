package com.ashishvz.atlys.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ashishvz.atlys.database.entities.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Item>)

    @Query("Select * from Items where invoiceId = :id")
    fun getItemForInvoice(id: String): LiveData<List<Item>>

    @Query("delete from Items where invoiceId = :id")
    fun deleteItemsForInvoice(id: String)
}