package com.rishabh.goodshows.showDetailsActivity.presenter

import com.google.gson.Gson
import com.rishabh.goodshows.MockResponses
import com.rishabh.goodshows.models.PaginatedResponse
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.network.TheMovieDbService
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SimilarShowsApiFailureTest {
    @Mock
    private lateinit var theMovieDbService: TheMovieDbService
    @Mock
    private lateinit var showDetailsView: ShowDetailsPresenter.View
    @Mock
    private lateinit var exception: Exception

    @InjectMocks
    private lateinit var showDetailsPresenter: ShowDetailsPresenter

    private val errorMessage = "HTTP 401 Unauthorized"
    private lateinit var tvShow: TvShow

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        val errorResponse = Flowable.create<PaginatedResponse<TvShow>>({
            it.onError(exception)
        }, BackpressureStrategy.DROP)

        Mockito.`when`(exception.message).thenReturn(errorMessage)

        tvShow = Gson().fromJson(MockResponses.TV_SHOW_JSON, TvShow::class.java)

        Mockito.`when`(theMovieDbService.getSimilarTvShows(tvShow.id!!))
                .thenReturn(errorResponse)

        showDetailsPresenter.attachView(showDetailsView)
    }

    @Test
    fun testFetchingPopularShows() {

        showDetailsPresenter.init(tvShow)

        Mockito.verify(showDetailsView, Mockito.times(1)).showProgress()
        Mockito.verify(showDetailsView, Mockito.after(200).times(1)).showError(errorMessage)
        Mockito.verify(showDetailsView, Mockito.after(200).times(1)).hideProgress()
    }

    @After
    fun tearDown() {
        showDetailsPresenter.detachView()
        showDetailsPresenter.destroy()
    }
}