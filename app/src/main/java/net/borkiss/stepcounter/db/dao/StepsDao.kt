package net.borkiss.stepcounter.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import net.borkiss.stepcounter.db.entity.Steps
import java.util.*

@Dao
interface StepsDao {
    @Query("SELECT * FROM Steps")
    fun getAll(): List<Steps>

    @Query("SELECT * FROM Steps WHERE date = :date")
    fun getStepsByDate(date: Date): Flowable<Steps>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSteps(steps: Steps): Completable

    @Query("DELETE FROM Steps")
    fun deleteAllSteps()
}