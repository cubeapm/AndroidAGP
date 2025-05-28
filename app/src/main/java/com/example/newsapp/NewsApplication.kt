package com.example.newsapp

import android.app.Application
import com.newrelic.agent.android.NewRelic
import com.newrelic.agent.android.logging.LogLevel

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NewRelic.withApplicationToken("AA9a15089936d85357e12515354e7ff46e965cf9c6-NRMA")
            .usingCollectorAddress("6e73-14-194-5-46.ngrok-free.app")
            .usingCrashCollectorAddress("6e73-14-194-5-46.ngrok-free.app")
            .withCrashReportingEnabled(true)
            .withLogLevel(4)
            .start(this)
    }
} 