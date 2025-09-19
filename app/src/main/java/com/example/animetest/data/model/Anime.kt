package com.example.animetest.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Anime(
    @SerialName("aired")
    val aired: Aired?,
    @SerialName("airing")
    val airing: Boolean?,
    @SerialName("approved")
    val approved: Boolean?,
    @SerialName("background")
    val background: String?,
    @SerialName("broadcast")
    val broadcast: Broadcast?,
    @SerialName("demographics")
    val demographics: List<Demographic>?,
    @SerialName("duration")
    val duration: String?,
    @SerialName("episodes")
    val episodes: Int?,
    @SerialName("explicit_genres")
    val explicitGenres: List<Genre?>?,
    @SerialName("favorites")
    val favorites: Int?,
    @SerialName("genres")
    val genres: List<Genre>?,
    @SerialName("images")
    val images: Images?,
    @SerialName("licensors")
    val licensors: List<Licensor>?,
    @SerialName("mal_id")
    val malId: Int,
    @SerialName("members")
    val members: Int?,
    @SerialName("popularity")
    val popularity: Int?,
    @SerialName("producers")
    val producers: List<Producer>?,
    @SerialName("rank")
    val rank: Int?,
    @SerialName("rating")
    val rating: String?,
    @SerialName("score")
    val score: Double?,
    @SerialName("scored_by")
    val scoredBy: Int?,
    @SerialName("season")
    val season: String?,
    @SerialName("source")
    val source: String?,
    @SerialName("status")
    val status: String?,
    @SerialName("studios")
    val studios: List<Studio>?,
    @SerialName("synopsis")
    val synopsis: String?,
    @SerialName("themes")
    val themes: List<Theme>?,
    @SerialName("title")
    val title: String?,
    @SerialName("title_english")
    val titleEnglish: String?,
    @SerialName("title_japanese")
    val titleJapanese: String?,
    @SerialName("title_synonyms")
    val titleSynonyms: List<String>?,
    @SerialName("titles")
    val titles: List<Title>?,
    @SerialName("trailer")
    val trailer: Trailer?,
    @SerialName("type")
    val type: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("year")
    val year: Int?
)