package net.borkiss.stepcounter.db.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.borkiss.stepcounter.db.dao.StepsDao
import net.borkiss.stepcounter.db.entity.Steps
import java.util.*

class StepsRepositoryImpl(private val stepsDao: StepsDao) : StepsRepository {
    override fun getStepsByDate(date: Date): Single<Steps> {
        return stepsDao.getStepsByDate(date)
            .firstOrError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    override fun addSteps(steps: Steps): Completable {
        return stepsDao.insertSteps(steps)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}