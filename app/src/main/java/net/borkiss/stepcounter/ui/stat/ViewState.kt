package net.borkiss.stepcounter.ui.stat

sealed class ViewState<T> {
    class Loading<T>: ViewState<T>()
    class Data<T>(val data: T): ViewState<T>()
    class Error<T>(val error: Throwable): ViewState<T>()
}