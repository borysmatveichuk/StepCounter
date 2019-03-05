package net.borkiss.stepcounter.db

import android.content.Context
import androidx.room.Room

fun createDb(context: Context): AppDatabase {
    return Room.databaseBuilder(context,
            AppDatabase::class.java, "steps-db"
        )
        .allowMainThreadQueries()
        .build()
}