package com.ashishvz.atlys.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ashishvz.atlys.R
import com.ashishvz.atlys.database.Status
import com.ashishvz.atlys.utils.DB_DATE_FORMAT
import com.ashishvz.atlys.utils.UI_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

@Entity(tableName = "Invoices")
data class Invoice(
    @PrimaryKey var invoiceId: String,
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
) {
    fun getPaymentDueDate(): String {
        return if (paymentDue == null) {
            "Due N/A"
        } else {
            val parser = SimpleDateFormat(DB_DATE_FORMAT, Locale.US)
            val formatter = SimpleDateFormat(UI_DATE_FORMAT, Locale.US)
            "Due " + formatter.format(parser.parse(paymentDue))
        }
    }

    fun getPaymentDueDateWithoutDue(): String {
        return if (paymentDue == null) {
            "N/A"
        } else {
            val parser = SimpleDateFormat(DB_DATE_FORMAT, Locale.US)
            val formatter = SimpleDateFormat(UI_DATE_FORMAT, Locale.US)
            formatter.format(parser.parse(paymentDue))
        }
    }

    fun getInvoiceDate(): String {
        return if (createdAt == null) {
            "N/A"
        } else {
            val parser = SimpleDateFormat(DB_DATE_FORMAT, Locale.US)
            val formatter = SimpleDateFormat(UI_DATE_FORMAT, Locale.US)
            formatter.format(parser.parse(createdAt))
        }
    }

    fun getFormattedInvoiceTotal(): String {
        return if (invoiceTotal == null) {
            "£ N/A"
        } else {
            "£ $invoiceTotal"
        }
    }

    fun setStatusBackgroundColor(): Int {
        return when (status) {
            Status.PAID -> {
                R.color.green_trans
            }
            Status.PENDING -> {
                R.color.orange_tran
            }
            else -> {
                R.color.light_blue_trans
            }
        }
    }

    fun setDotDrawable(): Int {
        return when (status) {
            Status.PAID -> {
                R.drawable.dot_green
            }
            Status.PENDING -> {
                R.drawable.dot
            }
            else -> {
                R.drawable.dot_draft
            }
        }
    }

    fun setStatusTextColor(): Int {
        return when (status) {
            Status.PAID -> {
                R.color.green
            }
            Status.PENDING -> {
                R.color.orange
            }
            else -> {
                R.color.light_grey
            }
        }
    }

    fun getFormattedStatus(): String {
        return when (status) {
            Status.PAID -> {
                Status.PAID.toString().substring(0,1).uppercase() + Status.PAID.toString().substring(1).lowercase()
            }
            Status.PENDING -> {
                Status.PENDING.toString().substring(0,1).uppercase() + Status.PENDING.toString().substring(1).lowercase()
            }
            else -> {
                Status.DRAFT.toString().substring(0,1).uppercase() + Status.DRAFT.toString().substring(1).lowercase()
            }
        }
    }

}
