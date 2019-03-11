package net.borkiss.stepcounter.ui.stat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import net.borkiss.stepcounter.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class StatFragment : Fragment() {

    companion object {
        fun newInstance() = StatFragment()
    }

    private val viewModel by viewModel<StatViewModel>()

    private lateinit var content: RecyclerView
    private lateinit var adapter: StatPagedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        content = view.findViewById(R.id.content)
        adapter = StatPagedAdapter()
        content.adapter = adapter
        content.layoutManager = LinearLayoutManager(context!!)
        content.addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.stepsPaged.observe(this, Observer {
            adapter.submitList(it)
        })
    }

//    @SuppressLint("SetTextI18n", "SimpleDateFormat")
//    private fun setViewState(viewState: ViewState<List<Steps>>) {
//        when (viewState) {
//            is ViewState.Loading -> {
//                results.text = "Loading..."
//            }
//            is ViewState.Data -> {
//                val dateParser = SimpleDateFormat("dd.MM.yyyy")
//                var allSteps = ""
//                for (step in viewState.data) {
//                    allSteps = "$allSteps${dateParser.format(step.date)} : ${step.count}\n"
//                }
//                results.text = if (allSteps.isEmpty()) {
//                    "No data yet."
//                } else {
//                    allSteps
//                }
//            }
//            is ViewState.Error -> {
//                results.text = "Error: ${viewState.error.localizedMessage}"
//            }
//        }
//    }

}
