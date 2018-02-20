package com.rishabh.goodshows.network

object NetworkConstants {
    val API_KEY = "5cf682705005da7f9159fe771e3cfb25"

    object ApiEndpoint {
        val POPULAR_TV = "/tv/popular"
        val BASE_URL = "https://api.themoviedb.org/3/"
        val SIMILAR_TV = "/tv/{tv_id}/similar"
    }

    object QueryParams {
        val API_KEY = "api_key"
        val LANGUAGE = "language"
        val PAGE = "page"
    }
}