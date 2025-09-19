package com.example.animetest.data.repository

import com.example.animetest.data.api.AnimeApi
import com.example.animetest.data.api.AnimeApiKtor
import com.example.animetest.data.model.Anime
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import javax.inject.Inject


interface AnimeRepository {
    suspend fun getTopAnime(): Result<List<Anime>>
}
interface AnimeRepositoryKtor {
    suspend fun getTopAnime(): Result<List<Anime>>
    suspend fun getAnimeById(animeId: Int): Result<Anime>
}

class AnimeRepositoryImpl @Inject constructor(
    private val animeApi: AnimeApi  // ← API, nie model
) : AnimeRepository {

    override suspend fun getTopAnime(): Result<List<Anime>> {
        return try {
            val response = animeApi.getTopAnime()
            if (response.isSuccessful) {
                val animeList = response.body()?.data ?: emptyList()
                Result.success(animeList)
            } else {
                println(" WIEPRZ2: ${response.code()}")
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            println(" WIEPRZ: $e")
            Result.failure(e)
        }
    }
}

class AnimeRepositoryKtorImpl @Inject constructor(
    private val animeApiKtor: AnimeApiKtor  // ← API, nie model
) : AnimeRepositoryKtor {
    override suspend fun getTopAnime(): Result<List<Anime>> {
        return try {
            val response = animeApiKtor.getTopAnime()
            Result.success(response.data)
        } catch (e: ClientRequestException) {
            println("WIEPRZ2: ${e.response.status.value}")
            Result.failure(Exception("API Error: ${e.response.status.value}"))
        } catch (e: ServerResponseException) {
            println("WIEPRZ2: ${e.response.status.value}")
            Result.failure(Exception("Server Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            println("WIEPRZ: $e")
            Result.failure(e)
        }
    }

    override suspend fun getAnimeById(animeId: Int): Result<Anime> {
        return try {
            val response = animeApiKtor.getAnimeById(animeId)
            Result.success(response.data)
        } catch (e: ClientRequestException) {
            println("WIEPRZ2: ${e.response.status.value}")
            Result.failure(Exception("API Error: ${e.response.status.value}"))
        } catch (e: ServerResponseException) {
            println("WIEPRZ2: ${e.response.status.value}")
            Result.failure(Exception("Server Error: ${e.response.status.value}"))
        } catch (e: Exception) {
            println("WIEPRZ: $e")
            Result.failure(e)
        }
    }
}