package com.example.newsapp

import android.app.Application
import com.newrelic.agent.android.NewRelic

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NewRelic.withApplicationToken("AA9a15089936d85357e12515354e7ff46e965cf9c6-NRMA")
            .usingCollectorAddress("a7b7-103-100-219-14.ngrok-free.app")
            .usingCrashCollectorAddress("a7b7-103-100-219-14.ngrok-free.app")
//            .withDistributedTraceListener("a7b7-103-100-219-14.ngrok-free.app")
            .withCrashReportingEnabled(true)
//            .withDistributedTracingEnabled(true)
            .withDT
            .start(this)
    }
} 