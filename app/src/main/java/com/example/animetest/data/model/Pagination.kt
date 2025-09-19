package com.example.animetest.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerialName("current_page")
    val currentPage: Int?,
    @SerialName("has_next_page")
    val hasNextPage: Boolean?,
    @SerialName("items")
    val items: Items?,
    @SerialName("last_visible_page")
    val lastVisiblePage: Int?
)