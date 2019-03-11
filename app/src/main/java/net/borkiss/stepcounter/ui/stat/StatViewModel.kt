package net.borkiss.stepcounter.ui.stat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import net.borkiss.stepcounter.db.entity.Steps
import net.borkiss.stepcounter.db.repository.StepsRepository

class StatViewModel(
    stepsRepository: StepsRepository
) : ViewModel() {

    val stepsPaged: LiveData<PagedList<Steps>>

    init {
        val factory = stepsRepository.getAllStepsPaged()
        val pagedListBuilder = LivePagedListBuilder<Int, Steps>(factory, 2)
        stepsPaged = pagedListBuilder.build()
    }
}
