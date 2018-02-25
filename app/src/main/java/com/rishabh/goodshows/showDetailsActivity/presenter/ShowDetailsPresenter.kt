package com.rishabh.goodshows.showDetailsActivity.presenter

import android.util.Log
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.rishabh.goodshows.models.PaginatedResponse
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.network.TheMovieDbService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ShowDetailsPresenter(private var theMovieDbService: TheMovieDbService) : MvpBasePresenter<ShowDetailsPresenter.View>() {

    private var tvShowId: Int = 0
    private var currentPage = 0
    private var pageAvailable = 1
    private var isLoading = false
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun init(tvShow: TvShow) {
        tvShowId = tvShow.id!!
        ifViewAttached { it.showProgress() }
        getSimilarTvShows()
    }

    fun getMoreSimilarShows() {
        ifViewAttached { it.showFooterLoader() }
        getSimilarTvShows()
    }

    private fun getSimilarTvShows() {
        if (currentPage < pageAvailable && !isLoading) {
            isLoading = true

            val disposable = theMovieDbService.getSimilarTvShows(tvShowId, currentPage + 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response -> onGetSimilarTvShowsSuccess(response) },
                            { e -> onGetSimilarTvShowsFailure(e) })

            compositeDisposable.add(disposable)
        }
    }

    private fun onGetSimilarTvShowsSuccess(response: PaginatedResponse<TvShow>?) {
        currentPage = response!!.page
        pageAvailable = response.totalPages

        isLoading = false
        ifViewAttached {
            it.hideProgress()
            it.hideFooterLoader()
            it.addSimilarTvShows(response.results)
        }
    }

    private fun onGetSimilarTvShowsFailure(e: Throwable?) {
        isLoading = false
        ifViewAttached {
            it.hideProgress()
            it.hideFooterLoader()
            it.showError(e!!.message!!)
            Log.e("Error", e.toString())
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
        fun showProgress()
        fun hideProgress()
        fun showFooterLoader()
        fun hideFooterLoader()
        fun addSimilarTvShows(similarTvShows: List<TvShow>)
        fun openTvShowDetailScreen(tvShow: TvShow)
        fun showError(errorMessage: String)
    }
}