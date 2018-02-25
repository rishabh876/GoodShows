package com.rishabh.goodshows.homeActivity.presenter

import android.util.Log
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.rishabh.goodshows.models.PaginatedResponse
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.network.TheMovieDbService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PopularShowsPresenter(private var theMovieDbService: TheMovieDbService) : MvpBasePresenter<PopularShowsPresenter.View>() {

    private var currentPage = 0
    private var pageAvailable = 1
    private var isLoading = false
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

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

            val disposable = theMovieDbService.getPopularTvShows(page = currentPage + 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response -> onGetPopularTvShowsSuccess(response) },
                            { e -> onGetPopularTvShowsFailure(e) })

            compositeDisposable.add(disposable)
        }
    }

    private fun onGetPopularTvShowsFailure(e: Throwable?) {
        isLoading = false
        ifViewAttached {
            it.hideFooterLoader()
            it.hideFullscreenProgress()
            it.showError(e!!.message!!)
            Log.e("Error", e.toString())
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

    fun onTvShowClicked(tvShow: TvShow) {
        ifViewAttached { it.openTvShowDetailScreen(tvShow) }
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    override fun destroy() {
        super.destroy()
        compositeDisposable.dispose()
    }

    interface View : MvpView {
        fun showFullscreenProgress()
        fun hideFullscreenProgress()
        fun showFooterLoader()
        fun hideFooterLoader()
        fun addItems(tvShows: List<TvShow>)
        fun showError(errorMessage: String)
        fun openTvShowDetailScreen(tvShow: TvShow)
    }
}