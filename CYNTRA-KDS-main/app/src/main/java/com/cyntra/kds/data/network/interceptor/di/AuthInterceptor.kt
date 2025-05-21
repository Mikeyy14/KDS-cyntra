package com.cyntra.kds.data.network.interceptor.di

import com.cyntra.kds.data.local.sharedPreference.SharedPreferenceUtil
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(val sharedPreferenceUtil: SharedPreferenceUtil) : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken= sharedPreferenceUtil.authToken
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Basic $authToken")
            .addHeader("Accept","application/json")
            .build()
        return chain.proceed(newRequest)
    }


}