package com.ramoncinp.relojcinbinariocompose.di

import com.ramoncinp.relojcinbinariocompose.data.network.TcpClient
import com.ramoncinp.relojcinbinariocompose.data.repository.DeviceCommunicator
import com.ramoncinp.relojcinbinariocompose.data.repository.DeviceCommunicatorImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideDeviceCommunicator(
        tcpClient: TcpClient,
        moshi: Moshi
    ): DeviceCommunicator =
        DeviceCommunicatorImpl(tcpClient, moshi)
}
