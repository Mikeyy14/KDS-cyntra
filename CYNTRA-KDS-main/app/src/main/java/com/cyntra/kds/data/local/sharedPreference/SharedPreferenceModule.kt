package com.cyntra.kds.data.local.sharedPreference

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {
    @Provides
    fun provideSharedPreferencesUtil(application: Application): SharedPreferenceUtil {
        return SharedPreferenceUtil(application)
    }
}