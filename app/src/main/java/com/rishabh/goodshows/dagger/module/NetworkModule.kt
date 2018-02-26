package com.rishabh.goodshows.dagger.module

import com.rishabh.goodshows.app.GoodShowsApplication
import com.rishabh.goodshows.network.NetworkConstants
import com.rishabh.goodshows.network.TheMovieDbService
import dagger.Module
import dagger.Provides
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
    fun provideMovieDbService(retrofit: Retrofit): TheMovieDbService {
        return retrofit.create<TheMovieDbService>(TheMovieDbService::class.java)
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