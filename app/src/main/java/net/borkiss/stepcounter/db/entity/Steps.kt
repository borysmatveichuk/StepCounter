package net.borkiss.stepcounter.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Steps (
    @PrimaryKey var date: Date,
    @ColumnInfo var count: Int
)