package com.rishabh.goodshows.dagger.module

import com.rishabh.goodshows.homeActivity.presenter.PopularShowsPresenter
import com.rishabh.goodshows.network.TheMovieDbService
import com.rishabh.goodshows.showDetailsActivity.presenter.ShowDetailsPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class PresentersModule {
    @Provides
    fun getHomePresenter(theMovieDbService: TheMovieDbService) = PopularShowsPresenter(theMovieDbService)

    @Provides
    fun getShowDetailPresenter(theMovieDbService: TheMovieDbService) = ShowDetailsPresenter(theMovieDbService)
}