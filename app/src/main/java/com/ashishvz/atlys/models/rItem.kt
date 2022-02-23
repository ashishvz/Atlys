package com.ashishvz.atlys.models

import com.ashishvz.atlys.database.entities.Item

data class rItem(
    val name: String?,
    val quantity: Int?,
    val price: Double?,
    val total: Double?
) {}

fun toItem(rItem: rItem, invoiceId: String): Item {
    return Item(
        0,
        invoiceId,
        rItem.name,
        rItem.quantity,
        rItem.price,
        rItem.total
    )
}
