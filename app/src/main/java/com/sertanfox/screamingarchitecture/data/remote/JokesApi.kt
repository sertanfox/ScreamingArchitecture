package com.sertanfox.screamingarchitecture.data.remote

import com.sertanfox.screamingarchitecture.data.remote.dto.CategoriesDto
import com.sertanfox.screamingarchitecture.data.remote.dto.RandomDto
import com.sertanfox.screamingarchitecture.data.remote.dto.SearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface JokesApi {
    @GET("search")
    suspend fun search(
        @Query("query") searchText:String
    ) : SearchDto
    @GET("categories")
    suspend fun categories(): CategoriesDto
    @GET("random")
    suspend fun random(): RandomDto

}