package net.borkiss.stepcounter.di

import net.borkiss.stepcounter.db.createDb
import net.borkiss.stepcounter.db.getStepsDao
import net.borkiss.stepcounter.db.repository.StepsRepository
import net.borkiss.stepcounter.db.repository.StepsRepositoryImpl
import org.koin.dsl.module.module

val dbModule = module {
    single { createDb(context = get()) }
    single { getStepsDao(db = get()) }
    single<StepsRepository> { StepsRepositoryImpl(stepsDao = get()) }
}