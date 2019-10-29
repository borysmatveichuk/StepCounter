package net.borkiss.stepcounter.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.borkiss.stepcounter.db.repository.StepsRepository
import java.util.*

@InternalCoroutinesApi
class MainViewModel(
    stepsRepository: StepsRepository
) : ViewModel() {

    val steps: MutableLiveData<Long> = MutableLiveData()

    init {
        viewModelScope.launch {
            stepsRepository.getStepsByDateFlow(Date())
                .map {
                    it.count.toLong()
                }.collect { steps ->
                    setSteps(steps)
                }
        }
    }

    private fun setSteps(value: Long) {
        steps.postValue(value)
    }
}