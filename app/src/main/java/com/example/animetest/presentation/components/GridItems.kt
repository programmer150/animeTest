package com.example.animetest.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.animetest.data.model.Anime

@Composable
fun GridItems(
    animeList: List<Anime>,
    modifier: Modifier = Modifier,
    onClickHandler: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = modifier,
        contentPadding = PaddingValues(20.dp),
    ) {
        items(animeList) {
            Card(
                modifier = Modifier.Companion
                    .size(width = 125.dp, height = 250.dp)
                    .padding(10.dp),
                onClick = { onClickHandler(it.malId) }
            ) {
                AsyncImage(
                    model = it.images?.jpg?.imageUrl,
                    contentDescription = it.title,
                    contentScale = ContentScale.Companion.Crop,
                    modifier = Modifier.Companion.fillMaxSize()
                )
            }
        }
    }
}