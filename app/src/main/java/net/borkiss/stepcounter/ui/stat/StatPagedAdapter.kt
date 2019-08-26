package net.borkiss.stepcounter.ui.stat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_steps.*
import net.borkiss.stepcounter.R
import net.borkiss.stepcounter.db.entity.Steps
import java.text.SimpleDateFormat

class StatPagedAdapter:  PagedListAdapter<Steps, StatPagedAdapter.StepsViewHolder>(StepsDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_steps, parent, false)
        return StepsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class StepsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        @SuppressLint("SimpleDateFormat")
        private val dateParser = SimpleDateFormat("dd.MM.yyyy")

        fun bind(steps: Steps) {
            date.text = dateParser.format(steps.date)
            count.text = steps.count.toString()
        }
    }
}

class StepsDiffCallback: DiffUtil.ItemCallback<Steps>() {
    override fun areItemsTheSame(oldItem: Steps, newItem: Steps): Boolean {
        return oldItem.date == newItem.date
                && oldItem.count == newItem.count
    }

    override fun areContentsTheSame(oldItem: Steps, newItem: Steps): Boolean {
        return oldItem == newItem
    }

}