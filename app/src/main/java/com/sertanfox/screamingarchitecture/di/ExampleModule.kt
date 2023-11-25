package com.sertanfox.screamingarchitecture.di

import com.sertanfox.screamingarchitecture.common.Constant
import com.sertanfox.screamingarchitecture.data.remote.ExampleApi
import com.sertanfox.screamingarchitecture.data.repository_impl.ExampleRepositoryImpl
import com.sertanfox.screamingarchitecture.domain.repository.ExampleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExampleModule {

    @Provides
    @Singleton
    fun providesExampleApi():ExampleApi {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExampleApi::class.java)
    }

    @Provides
    @Singleton
    fun providesExampleRepository(api:ExampleApi):ExampleRepository{
        return ExampleRepositoryImpl(api)
    }
}