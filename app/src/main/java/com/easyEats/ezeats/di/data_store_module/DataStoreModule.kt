package com.easyEats.ezeats.di.data_store_module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.easyEats.ezeats.utils.Constants.DATA_STORE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

// Use the preferencesDataStore delegate to create the DataStore
val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStorePreference(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
