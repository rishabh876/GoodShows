package com.rishabh.goodshows.dagger.module

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rishabh.goodshows.MockResponses
import com.rishabh.goodshows.app.GoodShowsApplication
import com.rishabh.goodshows.models.PaginatedResponse
import com.rishabh.goodshows.models.TvShow
import com.rishabh.goodshows.network.NetworkConstants
import com.rishabh.goodshows.network.TheMovieDbService
import dagger.Module
import dagger.Provides
import io.reactivex.Flowable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule(private var goodShowsApplication: GoodShowsApplication) {

    @Provides
    fun provideItemService(retrofit: Retrofit): TheMovieDbService {
        return object : TheMovieDbService {

            override fun getPopularTvShows(page: Int, apiKey: String): Flowable<PaginatedResponse<TvShow>> {
                val typeToken = object : TypeToken<PaginatedResponse<TvShow>>() {}.type
                return when (page) {
                    1 -> {
                        val successResponse = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
                        Flowable.just(successResponse)
                    }
                    2 -> {
                        val successResponsePage2 = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
                        Flowable.just(successResponsePage2)
                    }
                    else -> {
                        val successResponsePage3 = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
                        Flowable.just(successResponsePage3)
                    }
                }
            }

            override fun getSimilarTvShows(tvShowId: Int, page: Int, apiKey: String): Flowable<PaginatedResponse<TvShow>> {
                val typeToken = object : TypeToken<PaginatedResponse<TvShow>>() {}.type
                return when (page) {
                    1 -> {
                        val successResponse = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
                        Flowable.just(successResponse)
                    }
                    2 -> {
                        val successResponsePage2 = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
                        Flowable.just(successResponsePage2)
                    }
                    else -> {
                        val successResponsePage3 = Gson().fromJson<PaginatedResponse<TvShow>>(MockResponses.SUCCESS_JSON_PAGE1, typeToken)
                        Flowable.just(successResponsePage3)
                    }
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, @Named("baseUrl") baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return NetworkConstants.BASE_URL
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(goodShowsApplication.cacheDir, cacheSize.toLong())

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient().newBuilder()
                .cache(cache)
                .addInterceptor(logging)
                .build()
    }
}