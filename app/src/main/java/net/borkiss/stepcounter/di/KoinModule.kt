package net.borkiss.stepcounter.di

import net.borkiss.stepcounter.db.createDb
import net.borkiss.stepcounter.db.getStepsDao
import net.borkiss.stepcounter.db.repository.StepsRepository
import net.borkiss.stepcounter.db.repository.StepsRepositoryImpl
import net.borkiss.stepcounter.ui.main.MainViewModel
import net.borkiss.stepcounter.ui.stat.StatViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val dbModule = module {
    single { createDb(context = get()) }
    single { getStepsDao(db = get()) }
    single<StepsRepository> { StepsRepositoryImpl(stepsDao = get()) }
}

val viewModelsModule = module {
    viewModel { StatViewModel(stepsRepository = get()) }
    viewModel { MainViewModel(stepsRepository = get()) }
}