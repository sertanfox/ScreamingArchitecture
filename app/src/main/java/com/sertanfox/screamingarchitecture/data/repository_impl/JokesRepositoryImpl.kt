package com.sertanfox.screamingarchitecture.data.repository_impl

import com.sertanfox.screamingarchitecture.data.remote.JokesApi
import com.sertanfox.screamingarchitecture.data.remote.dto.CategoriesDto
import com.sertanfox.screamingarchitecture.data.remote.dto.RandomDto
import com.sertanfox.screamingarchitecture.data.remote.dto.SearchDto
import com.sertanfox.screamingarchitecture.domain.repository.jokes.JokesRepository
import javax.inject.Inject

class JokesRepositoryImpl @Inject constructor(
    private val api : JokesApi
) : JokesRepository {
    override suspend fun search(searchText: String): SearchDto {
       return api.search(searchText)
    }

    override suspend fun categories(): CategoriesDto {
        return api.categories()
    }

    override suspend fun random(): RandomDto {
        return api.random()
    }
}