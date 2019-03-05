package net.borkiss.stepcounter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import net.borkiss.stepcounter.db.AppDatabase
import net.borkiss.stepcounter.db.converter.DateConverter
import net.borkiss.stepcounter.db.entity.Steps
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test the implementation of [StepsDao]
 */
@RunWith(AndroidJUnit4::class)
class StepsDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getStepsWhenStepsNotInserted() {
        database.stepsDao().getStepsById(123)
            .test()
            .assertNoValues()
    }

    @Test
    fun insertAndGetSteps() {
        database.stepsDao().insertSteps(STEP_RECORD).blockingAwait()

        database.stepsDao().getStepsById(STEP_RECORD.id)
            .test()
            .assertValue {
                it.id == STEP_RECORD.id
                        && it.count == STEP_RECORD.count
                        && it.date == STEP_RECORD.date
            }
    }

    @Test
    fun updateAndGetSteps() {
        database.stepsDao().insertSteps(STEP_RECORD).blockingAwait()

        val updatedSteps = Steps(STEP_RECORD.id, 1000, STEP_RECORD.date)
        database.stepsDao().insertSteps(updatedSteps).blockingAwait()

        database.stepsDao().getStepsById(STEP_RECORD.id)
            .test()
            .assertValue {
                it.id == updatedSteps.id
                        && it.count == updatedSteps.count
                        && it.date == updatedSteps.date
            }
    }

    @Test
    fun deleteAndGetUser() {
        database.stepsDao().insertSteps(STEP_RECORD).blockingAwait()

        database.stepsDao().deleteAllSteps()
        database.stepsDao().getStepsById(STEP_RECORD.id)
            .test()
            .assertNoValues()
    }

    companion object {
        private val STEP_RECORD = Steps(0, 500, DateConverter.toDate("2015-05-16")!!)
    }
}
