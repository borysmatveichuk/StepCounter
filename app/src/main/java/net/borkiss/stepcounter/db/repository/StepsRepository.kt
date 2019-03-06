package net.borkiss.stepcounter.db.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import net.borkiss.stepcounter.db.entity.Steps
import java.util.*

interface StepsRepository {
    fun addSteps(steps: Steps): Completable
    fun getStepsByDate(date: Date): Single<Steps>
    fun getStepsByDateFlow(date: Date): Flowable<Steps>
}