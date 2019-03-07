package net.borkiss.stepcounter.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.db.repository.StepsRepository
import net.borkiss.stepcounter.ext.hasStepDetector
import net.borkiss.stepcounter.service.ERRORS_MESSAGES
import net.borkiss.stepcounter.service.ERROR_NO_STEP_DETECTOR
import net.borkiss.stepcounter.service.StepCountService
import net.borkiss.stepcounter.ui.stat.goToStat
import org.koin.android.ext.android.inject
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var stepCount: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var statButton: Button

    private val disposables = CompositeDisposable()

    private val stepsRepository: StepsRepository by inject()

    private fun setSteps(steps: Long) {
        stepCount.text = getString(R.string.Steps, steps, getDistanceRun(steps))
    }

    private fun getDistanceRun(steps: Long): Float {
        return (steps * 0.78).toFloat() / 1000.toFloat()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stepCount = findViewById(R.id.stepsCount)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        statButton = findViewById(R.id.statButton)

        startButton.setOnClickListener {
            if (!hasStepDetector()) {
                showError(ERROR_NO_STEP_DETECTOR)
                return@setOnClickListener
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, StepCountService::class.java))
            } else {
                startService(Intent(this, StepCountService::class.java))
            }
        }

        stopButton.setOnClickListener {
            stopService(Intent(this, StepCountService::class.java))
        }

        statButton.setOnClickListener{
            goToStat(this)
        }
    }

    override fun onResume() {
        super.onResume()
        disposables.add(
            stepsRepository.getStepsByDateFlow(Date())
                .map {
                    it.count.toLong()
                }
                .subscribe({
                    setSteps(it)
                }, {
                    setSteps(0)
                })
        )
        Toast.makeText(this, "Subscribed", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        disposables.dispose()
        Toast.makeText(this, "Unsubscribed", Toast.LENGTH_SHORT).show()
    }

    private fun showError(errorCode: Int) {
        val errorMsg = ERRORS_MESSAGES[errorCode]
        Toast.makeText(
            this@MainActivity,
            errorMsg!!,
            Toast.LENGTH_SHORT
        ).show()
    }
}
