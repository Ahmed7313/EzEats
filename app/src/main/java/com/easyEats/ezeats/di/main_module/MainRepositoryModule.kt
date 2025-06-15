package com.easyEats.ezeats.di.main_module

import com.easyEats.ezeats.data.repository.auth.repository.AuthRepositoryImpl
import com.easyEats.ezeats.data.local.data_store.DataStorePreference
import com.easyEats.ezeats.data.repository.main.repository.MainResponseImpl
import com.easyEats.ezeats.data.remote.auth.AuthApiService
import com.easyEats.ezeats.data.remote.auth.AuthWithParsing
import com.easyEats.ezeats.data.remote.main.MainApiService
import com.easyEats.ezeats.domain.auth.repository.AuthRepository
import com.easyEats.ezeats.domain.main.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class MainRepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        mainApiService: MainApiService,
        dataStorePreference: DataStorePreference
    ): MainRepository {
        return MainResponseImpl(mainApiService, dataStorePreference)
    }
}