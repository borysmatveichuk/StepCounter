package net.borkiss.stepcounter.db.converter

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*


object DateConverter {
    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd")

    @TypeConverter
    @JvmStatic
    fun toDate(value: String?): Date? {
        return if (value == null) null else formatter.parse(value)
    }

    @TypeConverter
    @JvmStatic
    fun toStringDate(date: Date?): String? {
        return formatter.format(date)
    }
}