package net.borkiss.stepcounter.ext

import android.content.Context

fun Context.hasStepDetector(): Boolean {
    with(packageManager) {
        if (!hasSystemFeature(android.content.pm.PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
            || !hasSystemFeature(android.content.pm.PackageManager.FEATURE_SENSOR_STEP_COUNTER)
        ) {
            return false
        }
    }
    return true
}