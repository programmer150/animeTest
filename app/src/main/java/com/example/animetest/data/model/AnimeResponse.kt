package com.example.animetest.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeResponse(
    @SerialName("data")
    val data: Anime
)