package com.sertanfox.screamingarchitecture.domain.usecase

import com.sertanfox.screamingarchitecture.common.Resource
import com.sertanfox.screamingarchitecture.data.remote.dto.ExampleDto
import com.sertanfox.screamingarchitecture.domain.repository.ExampleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ExampleUseCase @Inject constructor(
    private val repository: ExampleRepository
) {
    operator fun invoke(email: String, password:String): Flow<Resource<ExampleDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.login(email,password)
            emit(Resource.Success(response))
        } catch (ex:HttpException){
            emit(Resource.Error(ex.message ?: "An http error occurred."))
        } catch (ex:IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}