package net.borkiss.stepcounter.service

import net.borkiss.stepcounter.R

const val ERROR_NO_STEP_DETECTOR = 1
const val ERROR_UNKNOWN = 1000

val ERRORS_MESSAGES = mapOf(ERROR_NO_STEP_DETECTOR to R.string.ErrorNoStepDetector,
    ERROR_UNKNOWN to R.string.ErrorUnknown)

const val METERS_PER_STEP = 0.78f