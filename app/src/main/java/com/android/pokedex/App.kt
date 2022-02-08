package com.android.pokedex

import android.app.Application
import com.android.pokedex.data.api.network.SingleOkHttpClient

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
        SingleOkHttpClient.init(applicationContext)
    }
}