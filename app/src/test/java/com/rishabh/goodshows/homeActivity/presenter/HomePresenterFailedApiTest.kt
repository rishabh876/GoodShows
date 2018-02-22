package com.rishabh.goodshows.homeActivity.presenter

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
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * This test case testes the progress loader and ui updates on
 * Api failure of first set of results on home screen
 */

@RunWith(MockitoJUnitRunner::class)
class HomePresenterFailedApiTest {

    @Mock
    private lateinit var homeView: HomePresenter.View
    @Mock
    private lateinit var theMovieDbService: TheMovieDbService
    @Mock
    private lateinit var exception: Exception

    @InjectMocks
    private lateinit var homePresenter: HomePresenter

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        val errorResponse = Flowable.create<PaginatedResponse<TvShow>>({
            it.onError(exception)
            it.onComplete()
        }, BackpressureStrategy.DROP)

        Mockito.`when`(exception.message)
                .thenReturn("HTTP 401 Unauthorized")
        Mockito.`when`(theMovieDbService.getPopularTvShows())
                .thenReturn(errorResponse)

        homePresenter.attachView(homeView)
    }

    @Test
    fun testFetchingPopularShows() {

        homePresenter.init()

        Mockito.verify(homeView, Mockito.times(1)).showFullscreenProgress()
        Mockito.verify(homeView, Mockito.times(1)).showError(ArgumentMatchers.anyString())
        Mockito.verify(homeView, Mockito.times(1)).hideFullscreenProgress()
    }

    @After
    fun tearDown() {
        homePresenter.detachView()
        homePresenter.destroy()
    }
}