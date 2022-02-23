package com.ashishvz.atlys.models

import com.ashishvz.atlys.database.Status
import com.ashishvz.atlys.database.entities.Invoice

data class rInvoice(
    val id: String,
    val createdAt: String?,
    val paymentDue: String?,
    val description: String?,
    val paymentTerms: Int?,
    val clientName: String?,
    val clientEmail: String?,
    val status: String?,
    val senderAddress: SenderAddress?,
    val clientAddress: ClientAddress?,
    val items: List<rItem>?,
    val total: Double?
) {}

fun toInvoice(rInvoice: rInvoice): Invoice {
    return Invoice(
        0,
        rInvoice.id,
        rInvoice.createdAt,
        rInvoice.paymentDue,
        rInvoice.description,
        rInvoice.paymentTerms,
        rInvoice.clientName,
        rInvoice.clientEmail,
        Status.valueOf(rInvoice.status!!.toString().uppercase()),
        rInvoice.senderAddress!!.street,
        rInvoice.senderAddress.city,
        rInvoice.senderAddress.postCode,
        rInvoice.senderAddress.country,
        rInvoice.clientAddress!!.street,
        rInvoice.clientAddress.city,
        rInvoice.clientAddress.postCode,
        rInvoice.clientAddress.country,
        rInvoice.total
    )
}

