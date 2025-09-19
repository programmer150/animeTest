package com.example.animetest.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopAnimeResponse(
    @SerialName("data")
    val data: List<Anime>,
    @SerialName("pagination")
    val pagination: Pagination
)