package net.borkiss.stepcounter.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder

class StepCountService: Service() {

    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor
    private var steps: Long = 0

    private val sensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val sensor = event?.sensor

            if (sensor!!.type == Sensor.TYPE_STEP_DETECTOR) {
                steps++
                sendSteps(steps)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        sensorManager.registerListener(sensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(sensorEventListener)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun sendSteps(steps: Long) {
        val intent = Intent(BROADCAST_STEP)
        intent.putExtra(EXTRA_STEP_COUNT, steps)
        sendBroadcast(intent)
    }
}