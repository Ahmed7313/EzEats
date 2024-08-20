package com.easyEats.ezeats.di.auth_module

import com.easyEats.ezeats.data.repository.auth.repository.AuthRepositoryImpl
import com.easyEats.ezeats.data.local.data_store.DataStorePreference
import com.easyEats.ezeats.data.remote.auth.AuthApiService
import com.easyEats.ezeats.data.remote.auth.AuthWithParsing
import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        dataStorePreference: DataStorePreference,
        authWithParsing: AuthWithParsing,
        authApiService: AuthApiService
    ): AuthRepository {
        return AuthRepositoryImpl(dataStorePreference, authWithParsing, authApiService)
    }
}