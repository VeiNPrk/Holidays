package com.vnprk.holidays.di

import androidx.core.os.BuildCompat
import com.vnprk.holidays.InpApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vnprk.holidays.BuildConfig
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder()./*excludeFieldsWithoutExposeAnnotation().*/setLenient().create()

    @Provides
    fun provideInpApi(gson:Gson): InpApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(InpApi::class.java)
    }
}