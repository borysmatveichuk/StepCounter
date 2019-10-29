package net.borkiss.stepcounter.db.repository

import androidx.paging.DataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import net.borkiss.stepcounter.db.entity.Steps
import java.util.*

interface StepsRepository {
    suspend fun addSteps(steps: Steps): Long
    suspend fun getStepsByDate(date: Date): Steps
    fun getStepsByDateFlow(date: Date): Flow<Steps>
    fun getAllStepsPaged(): DataSource.Factory<Int, Steps>
}