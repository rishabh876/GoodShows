package com.rishabh.goodshows.showDetailsActivity.presenter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rishabh.goodshows.MockResponses
import com.rishabh.goodshows.models.PaginatedResponse
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.network.TheMovieDbService
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * This test checks the presenter for pagination.
 * A mock View replicates the behaviour of a view during pagination.
 */
@RunWith(MockitoJUnitRunner::class)
class SimilarShowsPaginationTest {

    @Mock
    private lateinit var theMovieDbService: TheMovieDbService
    @Mock
    private lateinit var showDetailsView: ShowDetailsPresenter.View

    @InjectMocks
    private lateinit var showDetailsPresenter: ShowDetailsPresenter
    private lateinit var tvShow: TvShow

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        val typeToken = object : TypeToken<PaginatedResponse<TvShow>>() {}.type
        val successResponse = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
        val responsePage1 = Flowable.just(successResponse)

        val successResponsePage2 = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
        val responsePage2 = Flowable.just(successResponsePage2)

        val successResponsePage3 = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
        val responsePage3 = Flowable.just(successResponsePage3)

        tvShow = Gson().fromJson(MockResponses.TV_SHOW_JSON, TvShow::class.java)

        Mockito.`when`(theMovieDbService.getSimilarTvShows(tvShow.id!!, 1))
                .thenReturn(responsePage1)
        Mockito.`when`(theMovieDbService.getSimilarTvShows(tvShow.id!!,2))
                .thenReturn(responsePage2)
        Mockito.`when`(theMovieDbService.getSimilarTvShows(tvShow.id!!,3))
                .thenReturn(responsePage3)

        showDetailsPresenter.attachView(showDetailsView)
    }

    @Test
    fun testFetchingPopularShows() {
        showDetailsPresenter.init(tvShow)

        Mockito.verify(showDetailsView, Mockito.times(1)).showProgress()
        Mockito.verify(showDetailsView, Mockito.after(100).times(1)).addSimilarTvShows(ArgumentMatchers.anyList())
        Mockito.verify(showDetailsView, Mockito.after(100).times(1)).hideProgress()

        Mockito.reset(showDetailsView)
        showDetailsPresenter.getMoreSimilarShows()

        Mockito.verify(showDetailsView, Mockito.times(1)).showFooterLoader()
        Mockito.verify(showDetailsView, Mockito.after(100).times(1)).addSimilarTvShows(ArgumentMatchers.anyList())
        Mockito.verify(showDetailsView, Mockito.after(100).times(1)).showFooterLoader()

        Mockito.reset(showDetailsView)
        showDetailsPresenter.getMoreSimilarShows()

        Mockito.verify(showDetailsView, Mockito.times(1)).showFooterLoader()
        Mockito.verify(showDetailsView, Mockito.after(100).times(1)).addSimilarTvShows(ArgumentMatchers.anyList())
        Mockito.verify(showDetailsView, Mockito.after(100).times(1)).showFooterLoader()
    }

    @After
    fun tearDown() {
        showDetailsPresenter.detachView()
        showDetailsPresenter.destroy()
    }
}