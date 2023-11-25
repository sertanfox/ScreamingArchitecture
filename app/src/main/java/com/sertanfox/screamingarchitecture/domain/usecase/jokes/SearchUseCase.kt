package com.sertanfox.screamingarchitecture.domain.usecase.jokes

import com.sertanfox.screamingarchitecture.common.Resource
import com.sertanfox.screamingarchitecture.data.remote.dto.SearchDto
import com.sertanfox.screamingarchitecture.domain.repository.jokes.JokesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: JokesRepository
) {
    operator fun invoke(searchText: String): Flow<Resource<SearchDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.search(searchText)
            emit(Resource.Success(response))
        } catch (ex: HttpException){
            emit(Resource.Error(ex.message ?: "An http error occurred."))
        } catch (ex: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}