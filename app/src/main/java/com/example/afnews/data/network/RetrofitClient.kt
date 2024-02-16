package com.example.afnews.data.network

import com.example.afnews.constant.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {
    private const val BASE_URL = Constants.BASE_URL

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val request =
            chain.request().newBuilder().addHeader("X-Api-Key", Constants.NEWS_API_KEY).build()
        chain.proceed(request)
    }.build()

    @Singleton
    @Provides
    fun provideRetrofitService(): NewsApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)


//    @Singleton
//    @Provides
//    val apiService: NewsApiService = retrofit.create(NewsApiService::class.java)
}