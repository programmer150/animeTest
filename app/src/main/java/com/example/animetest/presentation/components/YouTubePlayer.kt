package com.example.animetest.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun YouTubePlayer(
    youtubeVideoId: String?,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
    var youTubePlayerInstance by remember { mutableStateOf<YouTubePlayer?>(null) }

    LaunchedEffect(youTubePlayerInstance, youtubeVideoId) {
        delay(4000)
        youTubePlayerInstance?.play()
    }
    Box(
        modifier = modifier
    ) {
        AndroidView(
            modifier = Modifier.align(Alignment.TopEnd),
            factory = {
                YouTubePlayerView(context = it).apply {
                    lifecycleOwner.lifecycle.addObserver(this)

                    val iFramePlayerOptions = IFramePlayerOptions.Builder()
                        .controls(0) // ukryj kontrolki
                        .rel(0) // nie pokazuj powiązanych filmów
                        .autoplay(0) // nie odtwarzaj automatycznie
                        .build()
                    descendantFocusability = android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS
                    enableAutomaticInitialization = false
                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)
                            youTubePlayerInstance = youTubePlayer
                            youTubePlayer.mute()

                            youtubeVideoId?.let {
                                youTubePlayer.loadOrCueVideo(
                                    lifecycle = lifecycleOwner.lifecycle,
                                    videoId = youtubeVideoId,
                                    startSeconds = 0f
                                )
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(4000L) // 1.5 sekundy osteopenia dla dźwięku
                                youTubePlayer.unMute()
                            }

                        }
                    })
                    initialize(object : AbstractYouTubePlayerListener() {}, iFramePlayerOptions)
                }
            },
            update = { youTubePlayerView ->
                youTubePlayerInstance?.let { player ->
                    youtubeVideoId?.let {
                        player.cueVideo(
                            videoId = youtubeVideoId,
                            startSeconds = 0f
                        )
//                        player.loadOrCueVideo(
//                            lifecycle = lifecycleOwner.lifecycle,
//                            videoId = youtubeVideoId,
//                            startSeconds = 0f
//                        )
                    }
                }
            }

        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
//                        colors = listOf(
////                            Color.Transparent,              // Środek - pełna widoczność filmu
//                            Color.Transparent,              // Blisko środka - nadal przezroczysty
//                            Color.Black.copy(alpha = 0.1f), // Zaczyna się zaciemniać
//                            Color.Black.copy(alpha = 0.5f), // Średnie zaciemnienie
//                            Color.Black.copy(alpha = 0.6f), // Mocne zaciemnienie
//                            Color.Black.copy(alpha = 0.8f), // Prawie czarny
//                            Color.Black                     // Całkowicie czarny na krawędziach
//                        ),
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.3f to Color.Black.copy(alpha = 0.3f),
                            0.7f to Color.Black.copy(alpha = 0.7f),
                            1.0f to Color.Black
                        ),
                        center = Offset(Float.POSITIVE_INFINITY, 0f), // Prawy górny narożnik
                        radius = 1350f,
                        tileMode = TileMode.Clamp
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        startY = 0.0f,
                        endY = 40.0f,

                        colorStops = arrayOf(
                            0.0f to Color.Black,
                            0.5f to Color.Black.copy(alpha = 0.5f),
                            1.0f to Color.Transparent,
                        ),
                        tileMode = TileMode.Decal
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        startX = Float.POSITIVE_INFINITY,
                        endX = 0f,

                        colorStops = arrayOf(
                            0.0f to Color.Black,
                            0.2f to Color.Black.copy(alpha = 0.5f),
                            1.0f to Color.Transparent,
                        ),
                        tileMode = TileMode.Decal
                    )
                )
        )

//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    brush = Brush.radialGradient(
////                        colors = listOf(
//////                            Color.Transparent,              // Środek - pełna widoczność filmu
////                            Color.Transparent,              // Blisko środka - nadal przezroczysty
////                            Color.Black.copy(alpha = 0.1f), // Zaczyna się zaciemniać
////                            Color.Black.copy(alpha = 0.5f), // Średnie zaciemnienie
////                            Color.Black.copy(alpha = 0.6f), // Mocne zaciemnienie
////                            Color.Black.copy(alpha = 0.8f), // Prawie czarny
////                            Color.Black                     // Całkowicie czarny na krawędziach
////                        ),
//                        colorStops = arrayOf(
//                            0.0f to Color.Transparent,
//                            0.5f to Color.Transparent,
//                            0.9f to Color.Black.copy(alpha = 0.9f),
//                            1.0f to Color.Black
//                        ),
//                        center = Offset(0f, Float.POSITIVE_INFINITY), // Prawy górny narożnik
//                        radius = 1350f,
//                        tileMode = TileMode.Clamp
//                    )
//                )
//        )
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    brush = Brush.radialGradient(
//                        colors = listOf(
////                            Color.Transparent,              // Środek - pełna widoczność filmu
////                            Color.Transparent,              // Blisko środka - nadal przezroczysty
//                            Color.Black.copy(alpha = 0.1f), // Zaczyna się zaciemniać
//                            Color.Black                     // Całkowicie czarny na krawędziach
//                        ),
//                        center = Offset(0f, Float.POSITIVE_INFINITY), // Prawy górny narożnik
//                        radius = 1550f
//                    )
//                )
//        )

        // Overlay na dole - ukrywa kontrolki
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(80.dp)
//                .background(Color.Transparent)
//                .align(Alignment.BottomCenter)
//        )
    }
}