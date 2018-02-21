package com.rishabh.goodshows.dagger.module

import com.rishabh.goodshows.homeActivity.presenter.HomePresenter
import com.rishabh.goodshows.network.TheMovieDbService
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class PresentersModule {
    @Provides
    fun getHomePresenter(theMovieDbService: TheMovieDbService) = HomePresenter(theMovieDbService)
}