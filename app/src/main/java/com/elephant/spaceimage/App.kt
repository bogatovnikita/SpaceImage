package com.elephant.spaceimage

import android.app.Application
import retrofit2.Retrofit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        val retrofit = Retrofit.Builder()
    }
}