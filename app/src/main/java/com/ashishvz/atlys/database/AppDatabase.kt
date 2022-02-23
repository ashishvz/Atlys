package com.ashishvz.atlys.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ashishvz.atlys.database.dao.InvoiceDao
import com.ashishvz.atlys.database.dao.ItemDao
import com.ashishvz.atlys.database.entities.Invoice
import com.ashishvz.atlys.database.entities.Item
import com.ashishvz.atlys.utils.DATA_FILENAME
import com.ashishvz.atlys.utils.DB_NAME
import com.ashishvz.atlys.workers.PrepopulateDatabaseWorker
import com.ashishvz.atlys.workers.PrepopulateDatabaseWorker.Companion.KEY_FILENAME

@Database(entities = [Invoice::class, Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getInvoiceDao(): InvoiceDao
    abstract fun getItemDao(): ItemDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<PrepopulateDatabaseWorker>()
                                .setInputData(workDataOf(KEY_FILENAME to DATA_FILENAME))
                                .build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }
    }
}