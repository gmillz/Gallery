package com.gmillz.gallery.ui.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelInitializer
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.gmillz.gallery.data.Album
import ua.hospes.lazygrid.GridCells
import ua.hospes.lazygrid.LazyVerticalGrid
import ua.hospes.lazygrid.items

@Composable
fun GalleryScreen(
    album: String = "",
    model: GalleryViewModel = viewModel(key = album)
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(130.dp)
    ) {
        model.photos.value.keys.forEach {
            stickyHeader {
                Text(
                    text = it,
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                )
            }
            items(model.photos.value[it]!!) { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = photo.name,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(120.dp, 120.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }
    }
}