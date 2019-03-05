package net.borkiss.stepcounter.di

import net.borkiss.stepcounter.db.createDb
import org.koin.dsl.module.module


val dbModule = module {
    single { createDb(context = get()) }
}