package net.borkiss.stepcounter.db

import android.content.Context
import androidx.room.Room
import net.borkiss.stepcounter.db.dao.StepsDao

fun createDb(context: Context): AppDatabase {
    return Room.databaseBuilder(context,
            AppDatabase::class.java, "steps-db"
        )
        .allowMainThreadQueries()
        .build()
}

fun getStepsDao(db: AppDatabase): StepsDao {
    return db.stepsDao()
}