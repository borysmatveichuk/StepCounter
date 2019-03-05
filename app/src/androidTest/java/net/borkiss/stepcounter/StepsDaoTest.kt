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
import java.util.*

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
        database.stepsDao().getStepsByDate(Date())
            .test()
            .assertNoValues()
    }

    @Test
    fun insertAndGetSteps() {
        database.stepsDao().insertSteps(STEP_RECORD).blockingAwait()

        database.stepsDao().getStepsByDate(STEP_RECORD.date)
            .test()
            .assertValue {
                it.date == STEP_RECORD.date
                        && it.count == STEP_RECORD.count
            }

    }

    @Test
    fun updateAndGetSteps() {
        database.stepsDao().insertSteps(STEP_RECORD).blockingAwait()

        val updatedSteps = Steps(STEP_RECORD.date, 1000)
        database.stepsDao().insertSteps(updatedSteps).blockingAwait()

        database.stepsDao().getStepsByDate(STEP_RECORD.date)
            .test()
            .assertValue {
                it.date == updatedSteps.date
                        && it.count == updatedSteps.count

            }
    }

    @Test
    fun deleteAndGetSteps() {
        database.stepsDao().insertSteps(STEP_RECORD).blockingAwait()

        database.stepsDao().deleteAllSteps()
        database.stepsDao().getStepsByDate(STEP_RECORD.date)
            .test()
            .assertNoValues()
    }

    companion object {
        private val STEP_RECORD = Steps(DateConverter.toDate("2015-05-16")!!, 500)
    }
}
