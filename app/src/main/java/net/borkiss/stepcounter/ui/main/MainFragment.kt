package net.borkiss.stepcounter.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.*
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.ext.hasStepDetector
import net.borkiss.stepcounter.service.ERRORS_MESSAGES
import net.borkiss.stepcounter.service.ERROR_NO_STEP_DETECTOR
import net.borkiss.stepcounter.service.StepCountService
import net.borkiss.stepcounter.ui.stat.goToStat
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

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
            goToStat(context!!)
        }

        viewModel.steps.observe(viewLifecycleOwner, Observer{
            setSteps(it)
        })
    }

    private fun setSteps(steps: Long) {
        stepsCount.text = getString(R.string.Steps, steps, getDistanceRun(steps))
    }

    private fun getDistanceRun(steps: Long): Float {
        return (steps * 0.78).toFloat() / 1000.toFloat()
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