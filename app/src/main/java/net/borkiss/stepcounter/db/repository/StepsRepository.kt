package net.borkiss.stepcounter.db.repository

import androidx.paging.DataSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import net.borkiss.stepcounter.db.entity.Steps
import java.util.*

interface StepsRepository {
    fun addSteps(steps: Steps): Completable
    fun getStepsByDate(date: Date): Single<Steps>
    fun getStepsByDateFlow(date: Date): Flowable<Steps>
    fun getAllStepsFlow(): Flowable<List<Steps>>
    fun getAllStepsPaged(): DataSource.Factory<Int, Steps>
}