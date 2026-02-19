package com.example.newsapp

import android.app.Application
import android.util.Log
import com.newrelic.agent.android.NewRelic
import com.newrelic.agent.android.logging.LogLevel

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
//            NewRelic.withApplicationToken("AA9a15089936d85357e12515354e7ff46e965cf9c6-NRMA")
            NewRelic.withApplicationToken("AA84e6f0186b123ed3e0104c7c8e832400b9ac2caa-NRMA")
//                        NewRelic.withApplicationToken("fd46d41061b1b05f7ed00765030e2d5eFFFFNRAL")
//        NewRelic.withApplicationToken("eyJjdWJlLmVudmlyb25tZW50IjoiVU5TRVQiLCJzZXJ2aWNlLm5hbWUiOiJOZXdzIEFwcCIsInNlcnZpY2UudmVyc2lvbiI6IjEuMCIsInYiOiIxIn0")
//        NewRelic.withApplicationToken("UNSET")
            .usingCollectorAddress("homostyled-eustyle-annalisa.ngrok-free.dev")
            .usingCrashCollectorAddress("homostyled-eustyle-annalisa.ngrok-free.dev")
                .withCrashReportingEnabled(true)
                .withLogLevel(4)
                .start(this)
            Log.d("NewRelic", "NewRelic initialization successful")
        } catch (e: Exception) {
            Log.e("NewRelic", "Failed to initialize NewRelic", e)
        }
    }
}