package net.borkiss.stepcounter.db.repository

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.borkiss.stepcounter.db.dao.StepsDao
import net.borkiss.stepcounter.db.entity.Steps

class StepsRepositoryImpl(private val stepsDao: StepsDao) : StepsRepository {
    override fun addSteps(steps: Steps): Completable {
        return stepsDao.insertSteps(steps)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}