package com.rishabh.goodshows.network

object NetworkConstants {
    const val API_KEY = "5cf682705005da7f9159fe771e3cfb25"
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/"

    object ApiEndpoint {
        const val POPULAR_TV = "tv/popular"
        const val SIMILAR_TV = "tv/{" + PathParams.TV_SHOW_ID + "}/similar"
    }

    object QueryParams {
        const val API_KEY = "api_key"
        const val LANGUAGE = "language"
        const val PAGE = "page"
    }

    object PathParams {
        const val TV_SHOW_ID = "tv_id"
    }

    object ImageSize {
        const val W500 = "w500"
        const val W780 = "w780"
        const val W1280 = "w1280"
        const val ORIGINAL = "original"
    }
}