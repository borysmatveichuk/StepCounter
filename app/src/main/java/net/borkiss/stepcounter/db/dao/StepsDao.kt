package net.borkiss.stepcounter.db.dao

import androidx.paging.DataSource
import androidx.paging.PagedList
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
    @Query("SELECT * FROM Steps ORDER BY date DESC")
    fun getAll(): Flowable<List<Steps>>

    @Query("SELECT * FROM Steps ORDER BY date DESC")
    fun getAllPaged(): DataSource.Factory<Int, Steps>

    @Query("SELECT * FROM Steps WHERE date = :date")
    fun getStepsByDate(date: Date): Flowable<Steps>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSteps(steps: Steps): Completable

    @Query("DELETE FROM Steps")
    fun deleteAllSteps()
}