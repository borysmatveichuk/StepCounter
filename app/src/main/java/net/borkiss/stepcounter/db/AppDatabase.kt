package net.borkiss.stepcounter.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Steps::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun stepsDao(): StepsDao
}