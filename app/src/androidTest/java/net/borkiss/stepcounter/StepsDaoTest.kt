package net.borkiss.stepcounter

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import net.borkiss.stepcounter.db.AppDatabase
import net.borkiss.stepcounter.db.converter.DateConverter
import net.borkiss.stepcounter.db.entity.Steps
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Test the implementation of [StepsDao]
 */
@RunWith(AndroidJUnit4::class)
class StepsDaoTest {

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
    fun getStepsWhenStepsNotInserted() = runBlocking {
        val steps = database.stepsDao().getStepsByDate(Date())
        assertNull(steps)
    }

    @Test
    fun insertAndGetSteps() = runBlocking {
        database.stepsDao().insertSteps(STEP_RECORD)

        val actual = database.stepsDao().getStepsByDate(STEP_RECORD.date)
        assertEquals(STEP_RECORD, actual)
    }

    @Test
    fun updateAndGetSteps() = runBlocking {
        database.stepsDao().insertSteps(STEP_RECORD)

        val updatedSteps = Steps(STEP_RECORD.date, 1000)
        database.stepsDao().insertSteps(updatedSteps)

        val actual = database.stepsDao().getStepsByDate(STEP_RECORD.date)
        assertEquals(updatedSteps, actual)
    }

    @Test
    fun deleteAndGetSteps() = runBlocking {
        database.stepsDao().insertSteps(STEP_RECORD)

        database.stepsDao().deleteAllSteps()
        val actual = database.stepsDao().getStepsByDate(STEP_RECORD.date)
        assertNull(actual)
    }

    companion object {
        private val STEP_RECORD = Steps(DateConverter.toDate("2015-05-16")!!, 500)
    }
}
