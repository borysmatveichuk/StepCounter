package net.borkiss.stepcounter.ui.stat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable
import net.borkiss.stepcounter.db.entity.Steps
import net.borkiss.stepcounter.db.repository.StepsRepository

class StatViewModel(
    private val stepsRepository: StepsRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    val steps: MutableLiveData<ViewState<List<Steps>>> = MutableLiveData()

    fun loadSteps() {
        disposables.add(stepsRepository.getAllStepsFlow()
            .doOnSubscribe {
                steps.postValue(ViewState.Loading())
            }
            .subscribe({
                steps.postValue(ViewState.Data(it))
            }, {
                steps.postValue(ViewState.Error(it))
            }))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
