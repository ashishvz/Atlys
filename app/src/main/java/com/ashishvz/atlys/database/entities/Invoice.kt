package com.ashishvz.atlys.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ashishvz.atlys.database.Status

@Entity(tableName = "Invoices")
data class Invoice(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var invoiceId: String,
    var createdAt: String?,
    var paymentDue: String?,
    var description: String?,
    var paymentTerms: Int?,
    var clientName: String?,
    var clientEmail: String?,
    @TypeConverters(Status.Converter::class) var status: Status?,
    var sa_street: String?,
    var sa_city: String?,
    var sa_postCode: String?,
    var sa_country: String?,
    var ca_street: String?,
    var ca_city: String?,
    var ca_postCode: String?,
    var ca_country: String?,
    var invoiceTotal: Double?
)
