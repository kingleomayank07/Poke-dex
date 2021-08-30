package com.android.pokedex

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds

class App : Application() {
    private val TAG = App::class.simpleName

    companion object {
        private var app: App? = null
        fun getInstance(): App {
            return app!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}