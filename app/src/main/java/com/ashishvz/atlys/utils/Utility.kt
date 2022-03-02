package com.ashishvz.atlys.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.ashishvz.atlys.adapters.ItemListAdapter
import com.ashishvz.atlys.adapters.ItemsAdapter
import com.ashishvz.atlys.database.entities.Item
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


fun getInvoiceId(): String {
    return getRandomAlphabets() + getRandomNumbers()
}

fun getRandomNumbers(): String {
    return (1..4).map { (0..9).random() }
        .joinToString("")
}

fun getRandomAlphabets(): String {
    return (1..2).map { ('A'..'Z').random() }
        .joinToString("")
}

fun getEmptyItem(): Item {
    return Item(0, null, null, null, null, null)
}

@SuppressLint("SimpleDateFormat")
fun getDate(days: Int, invoiceDate: String): String {
    val calendar = Calendar.getInstance()
    calendar.time = SimpleDateFormat(
        UI_DATE_FORMAT
    ).parse(invoiceDate)!!
    calendar.add(Calendar.DATE, days)
    return SimpleDateFormat(DB_DATE_FORMAT).format(calendar.time)
}

fun doToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun getInvoiceTotal(itemListAdapter: ItemsAdapter): Double {
    val itemData = itemListAdapter.getItems()
    var total = 0.0
    if (itemData.isEmpty())
        return total
    for (item in itemData) {
        if (item.total != null)
            total += item.total!!
    }
    return total
}

fun getItemsToInsert(invoiceId: String, itemListAdapter: ItemsAdapter): List<Item>? {
    val itemList = itemListAdapter.getItems()
    val items = mutableListOf<Item>()
    for (item in itemList) {
        if (item.name != null && item.quantity != null && item.price != null && item.total != null) {
            item.invoiceId = invoiceId
            items.add(item)
        }
    }
    return items
}
