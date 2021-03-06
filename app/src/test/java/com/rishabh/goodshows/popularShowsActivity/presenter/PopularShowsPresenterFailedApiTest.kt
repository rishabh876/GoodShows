package com.rishabh.goodshows.popularShowsActivity.presenter

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

/**
 * This test case testes the progress loader and ui updates on
 * Api failure of first set of results on home screen
 */

@RunWith(MockitoJUnitRunner::class)
class PopularShowsPresenterFailedApiTest {

    @Mock
    private lateinit var popularShowsView: PopularShowsPresenter.View
    @Mock
    private lateinit var theMovieDbService: TheMovieDbService
    @Mock
    private lateinit var exception: Exception

    @InjectMocks
    private lateinit var popularShowsPresenter: PopularShowsPresenter

    private val errorMessage = "HTTP 401 Unauthorized"

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        val errorResponse = Flowable.create<PaginatedResponse<TvShow>>({
            it.onError(exception)
        }, BackpressureStrategy.DROP)

        Mockito.`when`(exception.message)
                .thenReturn(errorMessage)
        Mockito.`when`(theMovieDbService.getPopularTvShows())
                .thenReturn(errorResponse)

        popularShowsPresenter.attachView(popularShowsView)
    }

    @Test
    fun testFetchingPopularShows() {

        popularShowsPresenter.init()

        Mockito.verify(popularShowsView, Mockito.times(1)).showFullscreenProgress()
        Mockito.verify(popularShowsView, Mockito.after(200).times(1)).showError(errorMessage)
        Mockito.verify(popularShowsView, Mockito.after(200).times(1)).hideFullscreenProgress()
    }

    @After
    fun tearDown() {
        popularShowsPresenter.detachView()
        popularShowsPresenter.destroy()
    }
}