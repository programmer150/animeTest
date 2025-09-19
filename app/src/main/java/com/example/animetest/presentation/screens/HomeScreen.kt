package com.example.animetest.presentation.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.animetest.data.model.Anime
import com.example.animetest.presentation.viewmodel.AnimeViewModel
import com.example.animetest.presentation.viewmodel.UiState
import kotlinx.coroutines.delay
import androidx.compose.foundation.gestures.BringIntoViewSpec
import androidx.compose.foundation.gestures.LocalBringIntoViewSpec
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import com.example.animetest.presentation.components.YouTubePlayer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: AnimeViewModel = hiltViewModel(), onNavigateToDetails: (Int) -> Unit = {}
) {
    val animeState = viewModel.animeViewModel.collectAsState()

    when (val stateValue = animeState.value) {
        UiState.Error -> Text("Error")
        UiState.Loading -> CircularProgressIndicator()
//        is UiState.Success -> GridItems(
//            stateValue.animeList,
//            onClickHandler = { animeId -> onNavigateToDetails(animeId) })
        is UiState.Success -> HomeScreenLoaded(stateValue.animeList)
    }
}

@Composable
fun HomeScreenLoaded(animeList: List<Anime>, modifier: Modifier = Modifier) {
    val list = listOf(animeList, animeList, animeList, animeList, animeList, animeList)
    var focusedListIndex by remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState) {
//        lazyListState.isScrollInProgress
        println( " CZY JEST SCROLL? ${lazyListState.isScrollInProgress.toString()}")
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
    ) {
        itemsIndexed(list) { index, animeList ->
            val bringIntoViewRequester = remember { BringIntoViewRequester() }
            SampleImmersiveList(
                animeList,
                isFocused = index == focusedListIndex,
                onFocusState = { focusedListIndex = index },
                isFirst = index == 0,
                bringIntoViewRequester = bringIntoViewRequester
            )
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T : Anime> SampleImmersiveList(
    itemList: List<T>,
    isFocused: Boolean,
    onFocusState: () -> Unit,
    isFirst: Boolean,
    bringIntoViewRequester: BringIntoViewRequester
) {
    var selectedItem by remember { mutableStateOf<T?>(null) }
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

//    if (isFirst) {
//        LaunchedEffect(Unit) {
//            delay(1000)
//            focusRequester.requestFocus()
//        }
//    }

    LaunchedEffect(isFocused) {
        if (!isFocused)
            selectedItem = null
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .height(if (isFocused) 470.dp else 150.dp)
            .height(470.dp)
            .bringIntoViewRequester(bringIntoViewRequester)
            .onFocusChanged({ focusState ->
                if (focusState.hasFocus) {
                    onFocusState()
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView()
                    }

                }
            }),
    ) {
        if (selectedItem != null) {
            YouTubePlayer(
                youtubeVideoId = selectedItem?.trailer?.youtubeId,
                lifecycleOwner = LocalLifecycleOwner.current,
                modifier = Modifier
                    .height(450.dp)
//                    .width(800.dp)
                    .aspectRatio(14f / 9f)
                    .align(Alignment.TopEnd)

//                    .aspectRatio(20f / 7f)
            )
        } else{
            itemList[0].genres?.get(0)?.name?.let { Text(it) }
        }
        val horizontalPaddingPx = with(LocalDensity.current) { 40.dp.toPx() }
        CompositionLocalProvider(
            LocalBringIntoViewSpec provides FixFirstItemBringIntoViewSpec(
                startPaddingPx = horizontalPaddingPx
            )
        ) {
            LazyRow(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp),
            ) {
                itemsIndexed(itemList) { index, anime ->
                    Surface(
                        onClick = { }, modifier = Modifier
                            .width(200.dp)
                            .aspectRatio(16f / 9)
                            .then(
                                if (index == 0) Modifier.focusRequester(focusRequester)
                                else Modifier
                            )
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    println("ANIME ${anime.title}")
                                    selectedItem = anime
                                    coroutineScope.launch {
                                        bringIntoViewRequester.bringIntoView()
                                    }

                                }

                            }, border = ClickableSurfaceDefaults.border(
                            focusedBorder = Border(
                                border = BorderStroke(2.dp, Color.White),
                                inset = 4.dp,
                            )
                        )
                    ) {
                        AsyncImage(
                            model = anime.images?.jpg?.imageUrl,
                            contentDescription = anime.malId.toString(),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alpha = if (anime == selectedItem) 0.9f else 0.6f
                        )
                    }
                }
//                item { EmptyHorizontalCellScreen() }
            }
        }
        selectedItem?.let {
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                AnimeDescription(anime = selectedItem!!)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
class FixFirstItemBringIntoViewSpec(
    val startPaddingPx: Float
) : BringIntoViewSpec {
    override fun calculateScrollDistance(offset: Float, size: Float, containerSize: Float): Float {
        return offset - startPaddingPx
    }
}

@Composable
fun EmptyHorizontalCellScreen() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Box(
        modifier = Modifier
            .width(screenWidth)
            .background(Color.Transparent)
    ) {}
}

@Composable
fun AnimeDescription(anime: Anime, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.width(350.dp)
    ) {
        anime.title?.let {
            Text(
                it,
                style = MaterialTheme.typography.displaySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        anime.synopsis?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
        }
    }
}


//@OptIn(ExperimentalTvMaterial3Api::class)
//@Composable
//fun SampleImmersiveList(animeList: List<Anime>) {
////    val items = remember { listOf(Color.Red, Color.Green, Color.Yellow) }
//    var selectedItem by remember { mutableStateOf<Anime?>(null) }
//
//    // Container
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(400.dp)
//    ) {
//
//        selectedItem?.let {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(20f / 7)
//            ) {
//                FullscreenTrailer(youtubeVideoId = selectedItem!!.trailer?.youtubeId ?: "0") { }
//            }
//        }
//
//        // Rows
//        LazyRow(
//            modifier = Modifier.align(Alignment.BottomEnd),
//            horizontalArrangement = Arrangement.spacedBy(20.dp),
//            contentPadding = PaddingValues(20.dp),
//        ) {
//            items(animeList) { anime ->
//                Surface(
//                    onClick = { },
//                    modifier = Modifier
//                        .width(100.dp)
//                        .aspectRatio(9f / 16)
//                        .onFocusChanged {
//                            selectedItem = anime
//                        },
//                    border = ClickableSurfaceDefaults.border(
//                        focusedBorder = Border(
//                            border = BorderStroke(2.dp, Color.White),
//                            inset = 4.dp,
//                        )
//                    )
//                ) {
//                    AsyncImage(
//                        model = anime.images?.jpg?.imageUrl,
//                        contentDescription = anime.malId.toString(),
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//            }
//        }
//    }
//}

//@Composable
//fun YouTubePlayer2(
//    youtubeVideoId: String?,
//    lifecycleOwner: LifecycleOwner,
//    modifier: Modifier = Modifier
//) {
//    var youTubePlayerInstance by remember { mutableStateOf<YouTubePlayer?>(null) }
//
//    Surface(
//        modifier = modifier
//    ) {
//        AndroidView(
//            modifier = Modifier
//                .fillMaxSize()
//                .clip(RoundedCornerShape(10.dp)),
//            factory = {
//                YouTubePlayerView(context = it).apply {
//                    lifecycleOwner.lifecycle.addObserver(this)
//
//
//                    addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//                        override fun onReady(youTubePlayer: YouTubePlayer) {
//                            super.onReady(youTubePlayer)
//                            youTubePlayerInstance = youTubePlayer
//                            youtubeVideoId?.let {
//                                youTubePlayer.loadVideo(youtubeVideoId, 0f)
//                            }
//
//                        }
//                    })
//                }
//            },
//            update = { youTubePlayerView ->
//                youTubePlayerInstance?.let { player ->
//                    youtubeVideoId?.let {
//                        player.loadVideo(it, 0f)
//                    }
//                }
//            }
//
//        )
//    }
//}

//@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Composable
//fun SampleImmersiveList(
//    animeList: List<Anime>,
//    onFocusChange: (Anime) -> Unit,
//) {
//    val focusRequester = remember { FocusRequester() }
//
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }
//    LazyRow(
//        modifier = Modifier.focusProperties {
//
//        },
//        horizontalArrangement = Arrangement.spacedBy(20.dp),
//        contentPadding = PaddingValues(20.dp),
//
//        ) {
//        itemsIndexed(animeList) { index, anime ->
//            Surface(
//                onClick = { },
//                modifier = Modifier
//                    .width(100.dp)
//                    .aspectRatio(9f / 16)
//                    .onFocusChanged {
//                        if (it.hasFocus) {
//                            onFocusChange(anime)
//                        }
//                    }
//                    .then(
//                        if (index == 0) Modifier.focusRequester(focusRequester)
//                        else Modifier
//                    ),
//
//                border = ClickableSurfaceDefaults.border(
//                    focusedBorder = Border(
//                        border = BorderStroke(2.dp, Color.White),
//                        inset = 4.dp,
//                    )
//                )
//            ) {
//                AsyncImage(
//                    model = anime.images?.jpg?.imageUrl,
//                    contentDescription = anime.malId.toString(),
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//    }
//}
