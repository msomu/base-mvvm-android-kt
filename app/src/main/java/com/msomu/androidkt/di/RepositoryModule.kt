package com.msomu.androidkt.di

import com.msomu.androidkt.repository.Repository
import com.msomu.androidkt.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesRetrofit(repositoryImpl: RepositoryImpl): Repository = repositoryImpl

}