package com.sertanfox.screamingarchitecture.data.remote.dto


import com.google.gson.annotations.SerializedName
import com.sertanfox.screamingarchitecture.domain.models.jokes.SimpleResult

data class Result(
    @SerializedName("category")
    val category: List<String>?,
    @SerializedName("icon_url")
    val iconUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("value")
    val value: String
)

fun Result.toSimpleResult(): SimpleResult {
    return SimpleResult(
        category = category,
        value = value
    )
}