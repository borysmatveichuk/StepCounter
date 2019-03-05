package net.borkiss

import androidx.multidex.MultiDexApplication
import net.borkiss.stepcounter.di.dbModule
import org.koin.android.ext.android.startKoin

class StepCountApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(dbModule))
    }
}