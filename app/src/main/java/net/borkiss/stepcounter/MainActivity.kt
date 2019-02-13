package net.borkiss.stepcounter

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor
    private var steps: Long = 0

    private lateinit var stepCount: TextView

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val sensor = event?.sensor
        val values = event?.values
        var value = -1
        if (values?.size!! > 0) {
            value = values[0].toInt()
        }

        if (sensor!!.type == Sensor.TYPE_STEP_DETECTOR) {
            steps++
            setSteps(steps)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSteps(steps: Long) {
        stepCount.text = "Steps: $steps (${getDistanceRun(steps)}) m"
    }

    private fun getDistanceRun(steps: Long): Float {
        return (steps * 78).toFloat() / 100000.toFloat()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stepCount = findViewById(R.id.stepsCount)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this, stepSensor)
    }
}
