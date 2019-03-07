package net.borkiss.stepcounter.ui.stat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.db.entity.Steps
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat


class StatFragment : Fragment() {

    companion object {
        fun newInstance() = StatFragment()
    }

    private val viewModel by viewModel<StatViewModel>()

    private lateinit var results: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        results = view.findViewById(R.id.results)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.steps.observe(this, Observer {
            setViewState(it)
        })

        viewModel.loadSteps()
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setViewState(viewState: ViewState<List<Steps>>) {
        when (viewState) {
            is ViewState.Loading -> {
                results.text = "Loading..."
            }
            is ViewState.Data -> {
                val dateParser = SimpleDateFormat("dd.MM.yyyy")
                var allSteps = ""
                for (step in viewState.data) {
                    allSteps = "$allSteps${dateParser.format(step.date)} : ${step.count}\n"
                }
                results.text = if (allSteps.isEmpty()) {
                    "No data yet."
                } else {
                    allSteps
                }
            }
            is ViewState.Error -> {
                results.text = "Error: ${viewState.error.localizedMessage}"
            }
        }
    }

}
