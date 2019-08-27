package net.borkiss.stepcounter.ui.stat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import kotlinx.android.synthetic.main.stat_fragment.*
import net.borkiss.stepcounter.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class StatFragment : Fragment() {

    private val viewModel by viewModel<StatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StatPagedAdapter()
        content.adapter = adapter
        content.layoutManager = LinearLayoutManager(context!!)
        content.addItemDecoration(DividerItemDecoration(context, VERTICAL))

        viewModel.stepsPaged.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}
