package com.example.animetest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animetest.data.model.Anime


import com.example.animetest.data.repository.AnimeRepositoryKtor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UiState {
    data class Success(val animeList: List<Anime>) : UiState
    object Error : UiState
    object Loading : UiState
}

@HiltViewModel
class AnimeViewModel @Inject constructor(private val animeRepositoryKtor: AnimeRepositoryKtor) :
    ViewModel() {
    private val _animeViewModel: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val animeViewModel: StateFlow<UiState> = _animeViewModel.asStateFlow()

    init {
        getTopAnime()
    }

    private fun getTopAnime() {
        viewModelScope.launch {
            _animeViewModel.update { UiState.Loading }
            animeRepositoryKtor.getTopAnime().fold(
                onSuccess = { response ->
                    _animeViewModel.update { UiState.Success(response) }
                },
                onFailure = {
                    _animeViewModel.update { UiState.Error }
                }
            )
        }
    }
}