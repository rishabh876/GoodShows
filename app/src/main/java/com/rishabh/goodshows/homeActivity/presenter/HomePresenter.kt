package com.rishabh.goodshows.homeActivity.presenter

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.rishabh.goodshows.models.PaginatedResponse
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.network.TheMovieDbService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomePresenter(private var theMovieDbService: TheMovieDbService) : MvpBasePresenter<HomePresenter.View>() {

    private var currentPage = 0
    private var pageAvailable = 1
    private var isLoading = false

    fun init() {
        ifViewAttached {
            it.showFullscreenProgress()
        }
        getPopularTvShows()
    }

    fun getMoreTvShows() {
        ifViewAttached { it.showFooterLoader() }
        getPopularTvShows()
    }

    private fun getPopularTvShows() {
        if (currentPage < pageAvailable && !isLoading) {
            isLoading = true

            theMovieDbService.getPopularTvShows(page = currentPage + 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response -> onGetPopularTvShowsSuccess(response) },
                            { e -> onGetPopularTvShowsFailure(e) })
        }
    }

    private fun onGetPopularTvShowsFailure(e: Throwable?) {
        isLoading = false
        ifViewAttached {
            it.hideFooterLoader()
            it.hideFullscreenProgress()
        }
    }

    private fun onGetPopularTvShowsSuccess(response: PaginatedResponse<TvShow>) {
        currentPage = response.page
        pageAvailable = response.totalPages
        isLoading = false

        ifViewAttached {
            it.hideFooterLoader()
            it.hideFullscreenProgress()
            it.addItems(response.results)
        }
    }

    interface View : MvpView {
        fun showFullscreenProgress()
        fun hideFullscreenProgress()
        fun showFooterLoader()
        fun hideFooterLoader()
        fun addItems(tvShows: List<TvShow>)
    }
}