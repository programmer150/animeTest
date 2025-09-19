package com.example.animetest.data.api

import com.example.animetest.data.model.AnimeResponse
import com.example.animetest.data.model.TopAnimeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import retrofit2.Response
import retrofit2.http.GET


interface AnimeApi {
    @GET("top/anime")
    suspend fun getTopAnime() : Response<TopAnimeResponse>
}

interface AnimeApiKtor {
    suspend fun getTopAnime() : TopAnimeResponse
    suspend fun getAnimeById(animeId: Int) : AnimeResponse
}


class AnimeApiImpl(private val httpClient: HttpClient) : AnimeApiKtor {
    override suspend fun getTopAnime(): TopAnimeResponse {
        return httpClient.get("top/anime").body()
    }

    override suspend fun getAnimeById(animeId: Int): AnimeResponse {
        return httpClient.get("anime/$animeId").body()
    }
}