package net.borkiss.stepcounter.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.compose.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.MaterialColors
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.material.themeColor
import kotlinx.android.synthetic.main.main_fragment.*
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.ext.hasStepDetector
import net.borkiss.stepcounter.service.ERRORS_MESSAGES
import net.borkiss.stepcounter.service.ERROR_NO_STEP_DETECTOR
import net.borkiss.stepcounter.service.METERS_PER_STEP
import net.borkiss.stepcounter.service.StepCountService
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val viewModel by viewModel<MainViewModel>()
    private val state = StepCounterState()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.main_fragment_empty, container, false)

        (fragmentView as ViewGroup).setContent {
            composableContent(state)
        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.steps.observe(viewLifecycleOwner, Observer {
            //setSteps(it)
            state.updateSteps(it)
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

    private fun resolveColor(context: Context?, @AttrRes attrRes: Int, colorDefault: Color) =
        context?.let { Color(resolveThemeAttr(it, attrRes).data.toLong()) }
            ?: colorDefault

    private fun resolveThemeAttr(context: Context, @AttrRes attrRes: Int): TypedValue {
        val theme = context.theme
        val typedValue = TypedValue()
        theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue
    }


    @Model
    class StepCounterState(var steps: Long = 0) {
        fun updateSteps(value: Long) {
            steps = value
        }
    }

    @Composable
    fun composableContent(state: StepCounterState) = MaterialTheme(
        colors = MaterialColors(
            primary = resolveColor(context, R.attr.colorPrimary, MaterialColors().primary),
            secondary = resolveColor(context, R.attr.colorSecondary, MaterialColors().secondary)
        )
    ) {
        val intent = Intent(context, StepCountService::class.java)
        Surface(color = +themeColor { background }) {
            FlexColumn {
                expanded(1F) {
                    Column {
                        HeightSpacer(100.dp)
                        Center {
                            Button(
                                text = getString(R.string.Start),
                                onClick = {
                                    context?.let {
                                        if (it.hasStepDetector().not()) {
                                            showError(ERROR_NO_STEP_DETECTOR)
                                            return@Button
                                        }

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            context?.startForegroundService(intent)
                                        } else {
                                            context?.startService(intent)
                                        }
                                    }
                                }
                            )
                        }
                        HeightSpacer(16.dp)
                        Center {
                            Button(
                                text = getString(R.string.Stop),
                                onClick = {
                                    context?.stopService(intent)
                                }
                            )
                        }
                        HeightSpacer(16.dp)
                        Center {
                            Button(
                                text = getString(R.string.Stat),
                                onClick = {
                                    findNavController().navigate(R.id.action_mainFragment_to_statFragment)
                                }
                            )
                        }
                        HeightSpacer(16.dp)
                        showSteps(state)
                    }
                }
            }
        }
    }

    @Composable
    fun showSteps(state: StepCounterState) {
        Center {
            Text(
                text = getString(R.string.Steps, state.steps, getDistanceInKm(state.steps))
            )
        }
    }
}