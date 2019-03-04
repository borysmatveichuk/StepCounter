package net.borkiss.stepcounter.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StepsDao {
    @Query("SELECT * FROM steps")
    fun getAll(): List<Steps>
}