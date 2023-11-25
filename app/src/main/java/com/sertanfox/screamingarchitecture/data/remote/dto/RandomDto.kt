package com.sertanfox.screamingarchitecture.data.remote.dto


import com.google.gson.annotations.SerializedName

data class RandomDto(
    @SerializedName("icon_url")
    val iconUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("value")
    val value: String
)