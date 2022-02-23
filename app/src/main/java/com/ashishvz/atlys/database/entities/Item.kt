package com.ashishvz.atlys.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class Item(
    @PrimaryKey(autoGenerate = true) val itemId : Int,
    val invoiceId: String,
    var name: String?,
    var quantity: Int?,
    var price: Double?,
    var total: Double?
)
