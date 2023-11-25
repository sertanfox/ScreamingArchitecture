package com.sertanfox.screamingarchitecture.domain.repository

import com.sertanfox.screamingarchitecture.data.remote.dto.ExampleDto

interface ExampleRepository {
    suspend fun login(email:String,password:String):ExampleDto
}