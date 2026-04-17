package com.example.newsapp

import android.app.Application
import android.util.Log
import com.newrelic.agent.android.NewRelic
import com.newrelic.agent.android.logging.LogLevel

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {

            NewRelic.withApplicationToken("UNSET")
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
