package com.dmytrodanylyk

import android.app.Application
import com.dmytrodanylyk.examples.LongRunningJob

class MyApplication : Application() {

    val longRunningJob = LongRunningJob()

    override fun onCreate() {
        super.onCreate()

        // Coroutine debugging doesn't work yet, see:
        // https://stackoverflow.com/questions/50962243/debug-kotlins-coroutines-in-android
        System.setProperty("kotlinx.coroutines.debug", if (BuildConfig.DEBUG) "on" else "off")
    }
}