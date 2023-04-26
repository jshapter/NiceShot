package com.example.niceshot

import android.app.Application
import com.example.niceshot.data.AppContainer
import com.example.niceshot.data.AppDataContainer

class NiceShotApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}