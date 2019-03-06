package net.borkiss.stepcounter.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.db.entity.Steps
import net.borkiss.stepcounter.db.repository.StepsRepository
import org.joda.time.DateTime
import org.koin.android.ext.android.inject
import java.util.*


class StepCountService : Service() {
    private val TAG = StepCountService::class.java.simpleName

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var steps: Long = 0
    private var currentDay: Int = DateTime.now().dayOfYear

    private val stepsRepository: StepsRepository by inject()


    private val sensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val sensor = event?.sensor

            if (sensor!!.type == Sensor.TYPE_STEP_DETECTOR) {
                val timeInMillis = System.currentTimeMillis()
                resetCurrentDayAndStepsIfNewDay(timeInMillis)

                steps++
                Log.d(TAG, "TYPE_STEP_DETECTOR ${Date(timeInMillis)} ${event.values[0]}")
                saveSteps(timeInMillis, steps)
                sendSteps(steps)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun saveSteps(timeInMillis: Long, steps: Long) {
        val step = Steps(Date(timeInMillis), steps.toInt())
        stepsRepository.addSteps(step).subscribe()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        if (!hasStepDetector()) {
            sendError(ERROR_NO_STEP_DETECTOR)
            return
        }

        if (Build.VERSION.SDK_INT >= 26) {
            val channelId = StepCountService::class.java.simpleName
            val channel = NotificationChannel(
                channelId,
                getString(R.string.Notifications),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("").build()

            startForeground(1, notification)
        }

        initSteps()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        sensorManager?.registerListener(sensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)

        Toast.makeText(this, R.string.CounterStarted, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("CheckResult")
    private fun initSteps() {
        stepsRepository.getStepsByDate(Date())
            .map {
                steps = it.count.toLong()
            }
            .subscribe({
                Log.d(TAG, "Steps for ${Date()} $steps")
            },{
                Log.d(TAG, "No data yet for ${Date()}")
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(sensorEventListener)
        Toast.makeText(this, R.string.CounterStopped, Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun resetCurrentDayAndStepsIfNewDay(timeInMillis: Long) {
        val newDay = DateTime(timeInMillis).dayOfYear
        if (newDay != currentDay) {
            currentDay = newDay
            steps = 0
        }
    }

    private fun sendSteps(steps: Long) {
        val intent = Intent(BROADCAST_STEP)
        intent.putExtra(EXTRA_STEP_COUNT, steps)
        sendBroadcast(intent)
    }

    private fun sendError(errorCode: Int) {
        val intent = Intent(BROADCAST_STEP)
        intent.putExtra(EXTRA_ERROR_CODE, errorCode)
        sendBroadcast(intent)
    }

    private fun hasStepDetector(): Boolean {
        with(packageManager) {
            if (!hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
                || !hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
            ) {
                return false
            }
        }
        return true
    }
}
