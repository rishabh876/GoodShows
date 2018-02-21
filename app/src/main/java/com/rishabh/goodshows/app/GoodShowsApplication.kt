package com.rishabh.goodshows.app

import android.app.Application
import com.rishabh.goodshows.dagger.component.ApplicationComponent
import com.rishabh.goodshows.dagger.component.DaggerApplicationComponent
import com.rishabh.goodshows.dagger.module.NetworkModule
import com.rishabh.goodshows.dagger.module.PresentersModule

class GoodShowsApplication : Application() {

    companion object {
        lateinit var INSTANCE: GoodShowsApplication
            private set
    }

    lateinit var appComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initAppComponent()
    }

    private fun initAppComponent() {
        appComponent = DaggerApplicationComponent.builder()
                .networkModule(NetworkModule(this))
                .presentersModule(PresentersModule())
                .build()
    }

}