package net.borkiss.stepcounter.db.repository

import androidx.paging.DataSource
import net.borkiss.stepcounter.db.dao.StepsDao
import net.borkiss.stepcounter.db.entity.Steps
import java.util.*

class StepsRepositoryImpl(private val stepsDao: StepsDao) : StepsRepository {

    override suspend fun getStepsByDate(date: Date)=
        stepsDao.getStepsByDate(date)

    override fun getAllStepsPaged(): DataSource.Factory<Int, Steps> =
        stepsDao.getAllPaged()

    override fun getStepsByDateFlow(date: Date) =
        stepsDao.getStepsByDateFlow(date)

    override suspend fun addSteps(steps: Steps) =
        stepsDao.insertSteps(steps)

}