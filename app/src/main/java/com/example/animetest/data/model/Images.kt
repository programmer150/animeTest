package com.example.animetest.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Images(
    @SerialName("jpg")
    val jpg: Jpg?,
    @SerialName("webp")
    val webp: Webp?
)