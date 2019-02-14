package net.borkiss.stepcounter.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.service.EXTRA_STEP_COUNT
import net.borkiss.stepcounter.service.BROADCAST_STEP
import net.borkiss.stepcounter.service.StepCountService

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
        }
    }

    private val intentFilter = IntentFilter(BROADCAST_STEP)

    @SuppressLint("SetTextI18n")
    private fun setSteps(steps: Long) {
        stepCount.text = "Steps: $steps (${getDistanceRun(steps)}) Km"
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
            startService(Intent(this, StepCountService::class.java ))
            Toast.makeText(this, R.string.CounterStarted, Toast.LENGTH_SHORT).show()
        }

        stopButton.setOnClickListener {
            stopService(Intent(this, StepCountService::class.java ))
            Toast.makeText(this, R.string.CounterStopped, Toast.LENGTH_SHORT).show()
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
}
