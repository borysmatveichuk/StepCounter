package net.borkiss.stepcounter.db.repository

import io.reactivex.Completable
import net.borkiss.stepcounter.db.entity.Steps

interface StepsRepository {
    fun addSteps(steps: Steps): Completable
}