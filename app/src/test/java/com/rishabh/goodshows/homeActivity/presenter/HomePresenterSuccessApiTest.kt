package com.rishabh.goodshows.homeActivity.presenter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


/**
 * This test case testes the progress loader and ui updates on
 * Api success of first set of results on home screen
 */
@RunWith(MockitoJUnitRunner::class)
class HomePresenterSuccessApiTest {

    @Mock
    private lateinit var theMovieDbService: TheMovieDbService
    @Mock
    private lateinit var homeView: HomePresenter.View

    @InjectMocks
    private lateinit var homePresenter: HomePresenter

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        val typeToken = object : TypeToken<PaginatedResponse<TvShow>>() {}.type
        val successResponse = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
        val response = Flowable.just(successResponse)

        Mockito.`when`(theMovieDbService.getPopularTvShows())
                .thenReturn(response)

        homePresenter.attachView(homeView)
    }

    @Test
    fun testFetchingPopularShows() {
        homePresenter.init()

        verify(homeView, times(1)).showFullscreenProgress()
        verify(homeView, Mockito.after(100).times(1)).addItems(ArgumentMatchers.anyList())
        verify(homeView, Mockito.after(100).times(1)).hideFullscreenProgress()
    }

    @After
    fun tearDown() {
        homePresenter.detachView()
        homePresenter.destroy()
    }

}