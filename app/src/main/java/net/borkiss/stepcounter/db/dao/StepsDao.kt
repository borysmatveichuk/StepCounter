package net.borkiss.stepcounter.db.dao

import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import net.borkiss.stepcounter.db.entity.Steps
import java.util.*

@Dao
interface StepsDao {
    @Query("SELECT * FROM Steps ORDER BY date DESC")
    fun getAll(): Flow<List<Steps>>

    @Query("SELECT * FROM Steps ORDER BY date DESC")
    fun getAllPaged(): DataSource.Factory<Int, Steps>

    @Query("SELECT * FROM Steps WHERE date = :date")
    fun getStepsByDateFlow(date: Date): Flow<Steps>

    @Query("SELECT * FROM Steps WHERE date = :date")
    suspend fun getStepsByDate(date: Date): Steps

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: Steps): Long

    @Query("DELETE FROM Steps")
    suspend fun deleteAllSteps()
}