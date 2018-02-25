package com.rishabh.goodshows.popularShowsActivity.presenter

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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


/**
 * This test case testes the progress loader and ui updates on
 * Api success of first set of results on home screen
 */
@RunWith(MockitoJUnitRunner::class)
class PopularShowsPresenterSuccessApiTest {

    @Mock
    private lateinit var theMovieDbService: TheMovieDbService
    @Mock
    private lateinit var popularShowsView: PopularShowsPresenter.View

    @InjectMocks
    private lateinit var popularShowsPresenter: PopularShowsPresenter

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        val typeToken = object : TypeToken<PaginatedResponse<TvShow>>() {}.type
        val successResponse = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
        val response = Flowable.just(successResponse)

        Mockito.`when`(theMovieDbService.getPopularTvShows())
                .thenReturn(response)

        popularShowsPresenter.attachView(popularShowsView)
    }

    @Test
    fun testFetchingPopularShows() {
        popularShowsPresenter.init()

        verify(popularShowsView, times(1)).showFullscreenProgress()
        verify(popularShowsView, Mockito.after(200).times(1)).addItems(ArgumentMatchers.anyList())
        verify(popularShowsView, Mockito.after(200).times(1)).hideFullscreenProgress()
    }

    @After
    fun tearDown() {
        popularShowsPresenter.detachView()
        popularShowsPresenter.destroy()
    }

}