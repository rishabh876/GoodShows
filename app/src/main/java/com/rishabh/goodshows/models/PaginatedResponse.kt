package com.rishabh.goodshows.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaginatedResponse<T : Serializable> : Serializable {
    var page: Int = 1
    @SerializedName("total_results")
    var totalResults: Int = 0
    @SerializedName("total_pages")
    var totalPages: Int = 0

    lateinit var results: List<T>
}