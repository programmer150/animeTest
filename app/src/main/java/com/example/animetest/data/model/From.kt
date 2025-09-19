package com.example.animetest.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class From(
    @SerialName("day")
    val day: Int?,
    @SerialName("month")
    val month: Int?,
    @SerialName("year")
    val year: Int?
)