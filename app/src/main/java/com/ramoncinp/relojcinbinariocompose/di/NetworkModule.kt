package com.ramoncinp.relojcinbinariocompose.di

import com.ramoncinp.relojcinbinariocompose.data.network.DeviceScanner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideDeviceScanner(): DeviceScanner = DeviceScanner()
}