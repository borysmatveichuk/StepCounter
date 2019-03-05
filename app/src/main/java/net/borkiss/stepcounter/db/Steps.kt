package net.borkiss.stepcounter.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Steps (
    @PrimaryKey var id: Int,
    @ColumnInfo var count: Int
    // @ColumnInfo var date: Calendar
)