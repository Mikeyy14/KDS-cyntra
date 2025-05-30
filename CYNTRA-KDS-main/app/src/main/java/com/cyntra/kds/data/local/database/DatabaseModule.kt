package com.cyntra.kds.data.local.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun getAppDatabase(@ApplicationContext application: Context): AppDatabase
    {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, "AppDatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }


}


