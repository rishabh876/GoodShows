package com.rishabh.goodshows.network

import com.rishabh.goodshows.models.PaginatedResponse
import com.rishabh.goodshows.models.TvShow
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbService {

    @GET(NetworkConstants.ApiEndpoint.POPULAR_TV)
    fun getPopularTvShows(@Query(NetworkConstants.QueryParams.PAGE) page: Int = 1,
                          @Query(NetworkConstants.QueryParams.API_KEY) apiKey: String = NetworkConstants.API_KEY): Flowable<PaginatedResponse<TvShow>>

    @GET(NetworkConstants.ApiEndpoint.SIMILAR_TV)
    fun getSimilarTvShows(@Path(NetworkConstants.PathParams.TV_SHOW_ID) tvShowId: Int,
                          @Query(NetworkConstants.QueryParams.PAGE) page: Int = 1,
                          @Query(NetworkConstants.QueryParams.API_KEY) apiKey: String = NetworkConstants.API_KEY): Flowable<PaginatedResponse<TvShow>>
}