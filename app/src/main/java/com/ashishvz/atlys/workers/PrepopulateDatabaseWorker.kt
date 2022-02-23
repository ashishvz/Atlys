package com.ashishvz.atlys.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ashishvz.atlys.database.AppDatabase
import com.ashishvz.atlys.database.entities.Invoice
import com.ashishvz.atlys.models.rInvoice
import com.ashishvz.atlys.models.toInvoice
import com.ashishvz.atlys.models.toItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrepopulateDatabaseWorker(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    companion object {
        private const val TAG = "PrepopulateDatabase"
        const val KEY_FILENAME = "DATA_FILENAME"
    }


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_FILENAME)
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val type = object : TypeToken<List<rInvoice>>() {}.type
                        val invoiceList: List<rInvoice> = Gson().fromJson(jsonReader, type)
                        val database = AppDatabase.getInstance(applicationContext)
                        for (invoices in invoiceList) {
                           val invoice = toInvoice(invoices)
                            database.getInvoiceDao().insert(invoice)
                            for (item in invoices.items!!) {
                                val items = toItem(item, invoice.invoiceId!!)
                                database.getItemDao().insert(items)
                            }
                        }
                        Log.d(TAG,"Done")
                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }
}