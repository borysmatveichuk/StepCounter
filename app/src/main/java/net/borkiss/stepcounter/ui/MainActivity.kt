package net.borkiss.stepcounter.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.service.*

class MainActivity : AppCompatActivity() {

    private lateinit var stepCount: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val steps: Long? = intent?.getLongExtra(EXTRA_STEP_COUNT, 0)
            steps?.let {
                setSteps(it)
            }

            val error: Int? = intent?.getIntExtra(EXTRA_ERROR_CODE, 0)
            error?.let {
                if (it != 0) showError(it)
            }
        }
    }

    private val intentFilter = IntentFilter(BROADCAST_STEP)

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

        startButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, StepCountService::class.java))
            } else {
                startService(Intent(this, StepCountService::class.java))
            }
        }

        stopButton.setOnClickListener {
            stopService(Intent(this, StepCountService::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
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
