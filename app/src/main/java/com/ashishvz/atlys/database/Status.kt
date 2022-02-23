package com.ashishvz.atlys.database

import androidx.room.TypeConverter

enum class Status {
   DRAFT,
   PENDING,
   PAID;

   object Converter {
      @TypeConverter
      fun toInt(status: Status): Int = status.ordinal

      @TypeConverter
      fun fromInt(int: Int): Status = values()[int]
   }
}