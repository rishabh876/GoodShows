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
import org.mockito.junit.MockitoJUnitRunner

/**
 * This test checks the presenter for pagination.
 * A mock View replicates the behaviour of a view during pagination.
 */
@RunWith(MockitoJUnitRunner::class)
class PopularShowsPresenterPaginationTest {

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
        val responsePage1 = Flowable.just(successResponse)

        val successResponsePage2 = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
        val responsePage2 = Flowable.just(successResponsePage2)

        val successResponsePage3 = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
        val responsePage3 = Flowable.just(successResponsePage3)

        Mockito.`when`(theMovieDbService.getPopularTvShows(1))
                .thenReturn(responsePage1)
        Mockito.`when`(theMovieDbService.getPopularTvShows(2))
                .thenReturn(responsePage2)
        Mockito.`when`(theMovieDbService.getPopularTvShows(3))
                .thenReturn(responsePage3)

        popularShowsPresenter.attachView(popularShowsView)
    }

    @Test
    fun testFetchingPopularShows() {
        popularShowsPresenter.init()

        Mockito.verify(popularShowsView, Mockito.times(1)).showFullscreenProgress()
        Mockito.verify(popularShowsView, Mockito.after(100).times(1)).addItems(ArgumentMatchers.anyList())
        Mockito.verify(popularShowsView, Mockito.after(100).times(1)).hideFullscreenProgress()

        Mockito.reset(popularShowsView)
        popularShowsPresenter.getMoreTvShows()

        Mockito.verify(popularShowsView, Mockito.times(1)).showFooterLoader()
        Mockito.verify(popularShowsView, Mockito.after(100).times(1)).addItems(ArgumentMatchers.anyList())
        Mockito.verify(popularShowsView, Mockito.after(100).times(1)).showFooterLoader()

        Mockito.reset(popularShowsView)
        popularShowsPresenter.getMoreTvShows()

        Mockito.verify(popularShowsView, Mockito.times(1)).showFooterLoader()
        Mockito.verify(popularShowsView, Mockito.after(100).times(1)).addItems(ArgumentMatchers.anyList())
        Mockito.verify(popularShowsView, Mockito.after(100).times(1)).showFooterLoader()
    }

    @After
    fun tearDown() {
        popularShowsPresenter.detachView()
        popularShowsPresenter.destroy()
    }

}