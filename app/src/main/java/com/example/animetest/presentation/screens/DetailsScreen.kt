package com.example.animetest.presentation.screens

import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.tv.material3.Button
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.animetest.data.model.Anime
import com.example.animetest.presentation.viewmodel.AnimeDetailsUiState
import com.example.animetest.presentation.viewmodel.AnimeDetailsViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun DetailsScreen(animeId: Int, viewModel: AnimeDetailsViewModel = hiltViewModel()) {
    val detailAnimeState = viewModel.uiState.collectAsState()
    var showTrailer by remember { mutableStateOf(false) }

    LaunchedEffect(animeId) {
        viewModel.loadAnimeDetails(animeId)
    }

    when (val stateValue = detailAnimeState.value) {
        AnimeDetailsUiState.Error -> Text("Error")
        AnimeDetailsUiState.Loading -> CircularProgressIndicator()
        is AnimeDetailsUiState.Success -> {
            if (showTrailer) {
                // Fullscreen trailer
                FullscreenTrailer(
                    youtubeVideoId = stateValue.anime.trailer?.youtubeId ?: "",
                    onClose = { showTrailer = false }
                )
            } else {
                // Główna zawartość
                AnimeDetails(
                    anime = stateValue.anime,
                    onPlayTrailer = { showTrailer = true }
                )
            }
        }
    }
}

@Composable
fun AnimeDetails(
    anime: Anime,
    onPlayTrailer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tytuł
        Text(
            text = anime.title ?: "Unknown Title",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Zdjęcie anime
            AsyncImage(
                model = anime.images?.jpg?.largeImageUrl ?: anime.images?.jpg?.imageUrl,
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(200.dp)
                    .height(280.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Informacje i przycisk
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Ocena
                anime.score?.let { score ->
                    Text(
                        text = "Score: $score/10",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Status
                anime.status?.let { status ->
                    Text(
                        text = "Status: $status",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Rok
                anime.year?.let { year ->
                    Text(
                        text = "Year: $year",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Przycisk odtwórz trailer
                if (!anime.trailer?.youtubeId.isNullOrEmpty()) {
                    Button(
                        onClick = onPlayTrailer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),

                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Play Trailer",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Opis
        Text(
            text = "Synopsis:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = anime.synopsis ?: "No synopsis available",
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun FullscreenTrailer(
    youtubeVideoId: String,
    onClose: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var isFullscreen by remember { mutableStateOf(false) }
    var youTubePlayer by remember { mutableStateOf<YouTubePlayer?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Container dla fullscreen view
        var fullscreenContainer by remember { mutableStateOf<FrameLayout?>(null) }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val container = FrameLayout(context)
                fullscreenContainer = container

                val youTubePlayerView = YouTubePlayerView(context).apply {
                    // Wyłącz automatyczną inicjalizację
                    enableAutomaticInitialization = false

                    // Dodaj lifecycle observer
                    lifecycleOwner.lifecycle.addObserver(this)

                    // Konfiguracja z kontrolkami i fullscreen
                    val iFramePlayerOptions = IFramePlayerOptions.Builder()
                        .controls(0)
                        .fullscreen(0) // Ważne: włącz przycisk fullscreen
                        .build()

                    // Dodaj fullscreen listener
                    addFullscreenListener(object : FullscreenListener {
                        override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                            isFullscreen = true
                            // Ukryj normalny player i pokaż fullscreen view
                            visibility = View.GONE
                            container.addView(fullscreenView)
                        }


                        override fun onExitFullscreen() {
                            isFullscreen = false
                            // Pokaż normalny player i usuń fullscreen view
                            visibility = View.VISIBLE
                            container.removeAllViews()
                            container.addView(this@apply)
                        }
                    })

                    // Inicjalizuj z opcjami
                    initialize(object : AbstractYouTubePlayerListener() {
                        override fun onReady(player: YouTubePlayer) {
                            youTubePlayer = player
                            player.loadVideo(youtubeVideoId, 0f)
                        }

                        override fun onStateChange(
                            player: YouTubePlayer,
                            state: PlayerConstants.PlayerState
                        ) {
                            if (state == PlayerConstants.PlayerState.ENDED) {
                                onClose()
                            }
                        }
                    }, iFramePlayerOptions)
                }

                container.addView(youTubePlayerView)
                container
            }
        )
    }

    // Obsługa przycisku wstecz
    BackHandler(enabled = true) {
        if (isFullscreen) {
            // Jeśli w fullscreen, wyjdź z fullscreen
            youTubePlayer?.toggleFullscreen()
        } else {
            // Jeśli nie w fullscreen, zamknij całkowicie
            onClose()
        }
    }

    // Cleanup
    DisposableEffect(Unit) {
        onDispose {
            youTubePlayer?.pause()
        }
    }
}