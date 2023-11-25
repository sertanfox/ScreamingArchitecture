package com.sertanfox.screamingarchitecture.di

import com.sertanfox.screamingarchitecture.common.Constant
import com.sertanfox.screamingarchitecture.data.remote.JokesApi
import com.sertanfox.screamingarchitecture.data.repository_impl.JokesRepositoryImpl
import com.sertanfox.screamingarchitecture.domain.repository.jokes.JokesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExampleModule {

    val interceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .addHeader("X-RapidAPI-Key", Constant.API_KEY)
            .addHeader("X-RapidAPI-Host", Constant.API_HOST)
            .build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    fun providesJokesApi():JokesApi {

        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
            .build()
            .create(JokesApi::class.java)
    }

    @Provides
    @Singleton
    fun providesJokesRepository(api:JokesApi): JokesRepository {
        return JokesRepositoryImpl(api)
    }
}