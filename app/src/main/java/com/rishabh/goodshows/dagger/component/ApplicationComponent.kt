package com.rishabh.goodshows.dagger.component

import com.rishabh.goodshows.dagger.module.NetworkModule
import com.rishabh.goodshows.dagger.module.PresentersModule
import com.rishabh.goodshows.homeActivity.view.HomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, PresentersModule::class])
interface ApplicationComponent {

    fun inject(activity: HomeActivity)
}