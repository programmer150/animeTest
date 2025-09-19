package com.example.animetest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animetest.data.model.Anime
import com.example.animetest.data.repository.AnimeRepositoryKtor
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AnimeDetailsUiState {
    data class Success(val anime: Anime) : AnimeDetailsUiState
    object Error : AnimeDetailsUiState
    object Loading : AnimeDetailsUiState
}

@HiltViewModel
class AnimeDetailsViewModel @Inject constructor(
    private val animeRepositoryKtor: AnimeRepositoryKtor
) : ViewModel() {

    private val _uiState: MutableStateFlow<AnimeDetailsUiState> = MutableStateFlow(AnimeDetailsUiState.Loading)
    val uiState: StateFlow<AnimeDetailsUiState> = _uiState.asStateFlow()

    fun loadAnimeDetails(animeId: Int) {
        viewModelScope.launch {
            _uiState.update { AnimeDetailsUiState.Loading }
            animeRepositoryKtor.getAnimeById(animeId).fold(
                onSuccess = { anime ->
                    _uiState.update { AnimeDetailsUiState.Success(anime) }
                },
                onFailure = {
                    _uiState.update { AnimeDetailsUiState.Error }
                }
            )
        }
    }
}


