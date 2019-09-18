package net.borkiss.stepcounter.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.main_fragment.*
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.ext.hasStepDetector
import net.borkiss.stepcounter.service.ERRORS_MESSAGES
import net.borkiss.stepcounter.service.ERROR_NO_STEP_DETECTOR
import net.borkiss.stepcounter.service.METERS_PER_STEP
import net.borkiss.stepcounter.service.StepCountService
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel by viewModel<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(context, StepCountService::class.java)

        startButton.setOnClickListener {
            if (!context!!.hasStepDetector()) {
                showError(ERROR_NO_STEP_DETECTOR)
                return@setOnClickListener
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context?.startForegroundService(intent)
            } else {
                context?.startService(intent)
            }
        }

        stopButton.setOnClickListener {
            context?.stopService(intent)
        }

        statButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_statFragment)
        }

        viewModel.steps.observe(viewLifecycleOwner, Observer{
            setSteps(it)
        })
    }

    private fun setSteps(steps: Long) {
        stepsCount.text = getString(R.string.Steps, steps, getDistanceInKm(steps))
    }

    private fun getDistanceInKm(steps: Long): Float {
        return (steps * METERS_PER_STEP) / 1000f
    }

    private fun showError(errorCode: Int) {
        val errorMsg = ERRORS_MESSAGES[errorCode]
        Toast.makeText(
            context,
            errorMsg!!,
            Toast.LENGTH_SHORT
        ).show()
    }
}