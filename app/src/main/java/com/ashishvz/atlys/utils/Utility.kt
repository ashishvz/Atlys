package com.ashishvz.atlys.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.ashishvz.atlys.database.entities.Item
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


public fun getInvoiceId(): String {
    return getRandomAlphabets() + getRandomNumbers()
}

private fun getRandomNumbers(): String {
    return (1..4).map { (0..9).random() }
        .joinToString("")
}

private fun getRandomAlphabets(): String {
    return (1..2).map { ('A'..'Z').random() }
        .joinToString("")
}

private fun getEmptyItem(): Item {
    return Item(0, null, null, null, null, null)
}

@SuppressLint("SimpleDateFormat")
fun getDate(days: Int, invoiceDate: String): String {
    val calendar = Calendar.getInstance()
    calendar.time = SimpleDateFormat(
        DB_DATE_FORMAT
    ).parse(invoiceDate)!!
    calendar.add(Calendar.DATE, days)
    return SimpleDateFormat(DB_DATE_FORMAT).format(calendar.time)
}

fun doToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
