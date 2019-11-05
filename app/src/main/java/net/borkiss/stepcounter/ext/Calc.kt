package net.borkiss.stepcounter.ext

const val METERS_PER_STEP = 0.78f

fun getDistanceInKm(steps: Long): Float {
    return (steps * METERS_PER_STEP) / 1000f
}