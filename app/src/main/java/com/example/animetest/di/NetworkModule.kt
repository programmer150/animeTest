package com.example.animetest.di

import com.example.animetest.data.api.AnimeApi
import com.example.animetest.data.api.AnimeApiImpl
import com.example.animetest.data.api.AnimeApiKtor
import com.example.animetest.data.repository.AnimeRepository
import com.example.animetest.data.repository.AnimeRepositoryImpl
import com.example.animetest.data.repository.AnimeRepositoryKtor
import com.example.animetest.data.repository.AnimeRepositoryKtorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val BASE_URL = "https://api.jikan.moe/v4/"

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    @Singleton
    @Provides
    fun provideAnimeApi(retrofit: Retrofit) : AnimeApi {
        return retrofit.create(AnimeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAnimeRepository(animeApi: AnimeApi) : AnimeRepository {
        return AnimeRepositoryImpl(animeApi)
    }

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android){
            defaultRequest {
                url(BASE_URL)
            }
            install(ContentNegotiation){
                json(Json{
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Singleton
    @Provides
    fun provideAnimeApiKtor(httpClient: HttpClient): AnimeApiKtor {
        return AnimeApiImpl(httpClient)
    }

    @Singleton
    @Provides
    fun provideAnimeRepositoryKtor(animeApiKtor: AnimeApiKtor) : AnimeRepositoryKtor {
        return AnimeRepositoryKtorImpl(animeApiKtor)
    }




}