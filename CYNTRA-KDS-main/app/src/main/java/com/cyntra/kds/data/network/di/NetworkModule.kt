package com.cyntra.kds.data.network.di

import com.cyntra.kds.constants.network.BASE_URL
import com.cyntra.kds.data.network.interceptor.qualifiers.WithInterceptor
import com.cyntra.kds.data.network.interceptor.qualifiers.WithoutInterceptor
import com.cyntra.kds.data.network.service.LoginService
import com.cyntra.kds.data.local.sharedPreference.SharedPreferenceUtil
import com.cyntra.kds.data.network.interceptor.di.AuthInterceptor
import com.cyntra.kds.data.network.service.CrashErrorService
import com.cyntra.kds.data.network.service.SessionService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{

    @Provides
    @Singleton
    fun provideAuthInterceptor(sharedPreferenceUtil: SharedPreferenceUtil) = AuthInterceptor(sharedPreferenceUtil)

    @Provides
    @Singleton
    fun providesGson() = Gson()

    @Provides
    @Singleton
    @WithInterceptor
    fun provideOkHttpWithInterceptor(authInterceptor: AuthInterceptor): OkHttpClient {
        val okhttpBuilders = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return okhttpBuilders.build()
    }


    @Provides
    @Singleton
    @WithoutInterceptor
    fun provideOkHttpWithoutInterceptor(): OkHttpClient {
        val okhttpBuilders = OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return okhttpBuilders.build()
    }


    @Provides
    @Singleton
    @WithInterceptor
    fun provideInstanceOfRetrofitWithInterceptor(
        @WithInterceptor okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()


    @Provides
    @Singleton
    @WithoutInterceptor
    fun provideInstanceOfRetrofitWithoutInterceptor(
        @WithoutInterceptor okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()

    @Provides
    @Singleton
    fun providesLoginService(
        @WithoutInterceptor retrofit: Retrofit,
    ): LoginService = retrofit.create(LoginService::class.java)

    @Singleton
    @Provides
    fun providesSessionCloseService(
        @WithInterceptor retrofit: Retrofit,
    ): SessionService = retrofit.create(SessionService::class.java)


    @Singleton
    @Provides
    fun providesCrashErrorService(
        @WithInterceptor retrofit: Retrofit,
    ): CrashErrorService = retrofit.create(CrashErrorService::class.java)
}