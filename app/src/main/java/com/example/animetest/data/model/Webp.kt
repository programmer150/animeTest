package com.example.animetest.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Webp(
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("large_image_url")
    val largeImageUrl: String?,
    @SerialName("small_image_url")
    val smallImageUrl: String?
)