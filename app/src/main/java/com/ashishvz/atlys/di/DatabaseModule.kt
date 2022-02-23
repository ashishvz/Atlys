package com.ashishvz.atlys.di

import android.content.Context
import com.ashishvz.atlys.database.AppDatabase
import com.ashishvz.atlys.database.dao.InvoiceDao
import com.ashishvz.atlys.database.dao.ItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideInvoiceDao(appDatabase: AppDatabase): InvoiceDao {
        return appDatabase.getInvoiceDao()
    }

    @Provides
    fun provideItemDao(appDatabase: AppDatabase): ItemDao {
        return appDatabase.getItemDao()
    }
}