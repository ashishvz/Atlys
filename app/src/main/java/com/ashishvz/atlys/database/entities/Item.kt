package com.ashishvz.atlys.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class Item(
    @PrimaryKey(autoGenerate = true) val itemId : Int,
    var invoiceId: String?,
    var name: String?,
    var quantity: Int?,
    var price: Double?,
    var total: Double?
) {
    fun getItemQtyAndPrice(): String {
        return quantity.toString() + " x £" + price
    }

    fun getItemTotal(): String {
        return "£$total"
    }
}
