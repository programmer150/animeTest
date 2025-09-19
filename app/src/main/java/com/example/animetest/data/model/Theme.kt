package com.example.animetest.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Theme(
    @SerialName("mal_id")
    val malId: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("type")
    val type: String?,
    @SerialName("url")
    val url: String?
)