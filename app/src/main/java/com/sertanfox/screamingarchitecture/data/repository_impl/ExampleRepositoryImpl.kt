package com.sertanfox.screamingarchitecture.data.repository_impl

import com.sertanfox.screamingarchitecture.data.remote.ExampleApi
import com.sertanfox.screamingarchitecture.data.remote.dto.ExampleDto
import com.sertanfox.screamingarchitecture.domain.repository.ExampleRepository
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val api : ExampleApi
) : ExampleRepository {
    override suspend fun login(email: String, password: String): ExampleDto {
        return api.login(email,password)
    }
}