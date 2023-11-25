package com.sertanfox.screamingarchitecture.data.remote

import com.sertanfox.screamingarchitecture.data.remote.dto.ExampleDto
import retrofit2.http.GET

interface ExampleApi {

    @GET("test/login")
    suspend fun login(email:String,password:String):ExampleDto

}