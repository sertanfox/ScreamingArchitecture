package com.sertanfox.screamingarchitecture.data.remote.dto


import com.google.gson.annotations.SerializedName

data class SearchDto(
    @SerializedName("result")
    val result: List<Result>,
    @SerializedName("total")
    val total: Int
)