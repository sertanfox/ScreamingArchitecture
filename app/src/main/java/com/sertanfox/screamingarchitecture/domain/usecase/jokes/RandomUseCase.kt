package com.sertanfox.screamingarchitecture.domain.usecase.jokes
import com.sertanfox.screamingarchitecture.common.Resource
import com.sertanfox.screamingarchitecture.data.remote.dto.RandomDto
import com.sertanfox.screamingarchitecture.domain.repository.jokes.JokesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RandomUseCase @Inject constructor(
    private val repository: JokesRepository
) {
    operator fun invoke(): Flow<Resource<RandomDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.random()
            emit(Resource.Success(response))
        } catch (ex: HttpException){
            emit(Resource.Error(ex.message ?: "An http error occurred."))
        } catch (ex: IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}