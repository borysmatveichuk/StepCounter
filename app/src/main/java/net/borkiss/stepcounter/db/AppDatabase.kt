package net.borkiss.stepcounter.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.borkiss.stepcounter.db.entity.Steps
import net.borkiss.stepcounter.db.converter.DateConverter
import net.borkiss.stepcounter.db.dao.StepsDao

@Database(entities = [Steps::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun stepsDao(): StepsDao
}