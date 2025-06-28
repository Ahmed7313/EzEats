package com.easyEats.ezeats.di.network_module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.easyEats.ezeats.utils.headers.HeaderInterceptor
import com.easyEats.ezeats.BuildConfig
import com.easyEats.ezeats.data.local.data_store.DataStorePreference
import com.easyEats.ezeats.data.remote.auth.AuthApiService
import com.easyEats.ezeats.data.remote.main.MainApiService
import com.easyEats.ezeats.utils.headers.CacheInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    /**
     * Provides Gson instance for JSON serialization/deserialization.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    /**
     * Provides GsonConverterFactory for Retrofit.
     */
    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    /**
     * Provides HttpLoggingInterceptor for logging HTTP request/response details.
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }


    /**
     * Provides a custom com.easyEats.ezeats.utils.headers.HeaderInterceptor for adding headers to requests.
     */
    @Provides
    @Singleton
    fun provideHeaderInterceptor(dataStorePreference: DataStorePreference): HeaderInterceptor {
        return HeaderInterceptor(dataStorePreference)
    }

    /**
     * Provides OkHttpClient for making HTTP requests.
     */

    class RetryInterceptor(private val maxRetry: Int) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var attempt = 0
            var response: Response
            var exception: Exception? = null

            do {
                try {
                    response = chain.proceed(chain.request())
                    if (response.isSuccessful) {
                        return response
                    }
                } catch (e: Exception) {
                    exception = e
                }
                attempt++
            } while (attempt < maxRetry)

            throw exception ?: IOException("Request failed after $maxRetry attempts")
        }
    }

    @Provides
    @Singleton
    fun provideUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            val sslSocketFactory = sslContext.socketFactory
            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor,
        unsafeOkHttpClient: OkHttpClient.Builder,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return unsafeOkHttpClient
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
//            .addInterceptor(RetryInterceptor(maxRetry = 2))
            .addNetworkInterceptor(CacheInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Provides Retrofit instance for API calls.
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    /**
     * Provides Auth API service interface implementation.
     */
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    /**
     * Provides Main API service interface implementation.
     */
    @Provides
    @Singleton
    fun provideMainApiService(retrofit: Retrofit): MainApiService {
        return retrofit.create(MainApiService::class.java)
    }


}
