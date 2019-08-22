package net.borkiss.stepcounter.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.borkiss.stepcounter.db.repository.StepsRepository
import java.util.*

class MainViewModel(
    stepsRepository: StepsRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()
    val steps: MutableLiveData<Long> = MutableLiveData()

    init {
        stepsRepository.getStepsByDateFlow(Date())
            .map {
                it.count.toLong()
            }
            .subscribe({
                setSteps(it)
            }, {
                setSteps(0)
            }).apply { disposables.add(this) }
    }

    private fun setSteps(value: Long) {
        steps.postValue(value)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}