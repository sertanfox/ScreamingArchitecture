package com.sertanfox.screamingarchitecture.domain.repository.jokes

import com.sertanfox.screamingarchitecture.data.remote.dto.CategoriesDto
import com.sertanfox.screamingarchitecture.data.remote.dto.RandomDto
import com.sertanfox.screamingarchitecture.data.remote.dto.SearchDto

interface JokesRepository {
    suspend fun search(searchText:String): SearchDto
    suspend fun categories(): CategoriesDto
    suspend fun random(): RandomDto

}