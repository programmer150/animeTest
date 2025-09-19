package com.example.animetest.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Broadcast(
    @SerialName("day")
    val day: String?,
    @SerialName("string")
    val string: String?,
    @SerialName("time")
    val time: String?,
    @SerialName("timezone")
    val timezone: String?
)