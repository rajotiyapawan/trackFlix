package com.rajotiyapawan.trackflix.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rajotiyapawan.trackflix.R

@Composable
fun Modifier.noRippleClick(delayMillis: Long = 500L, onClick: () -> Unit): Modifier {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return this.then(
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= delayMillis) {
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}

@Composable
fun ImageFromUrl(modifier: Modifier = Modifier, imageUrl: String, contentScale: ContentScale = ContentScale.Fit) {
    val context = LocalContext.current
    if (isNetworkAvailable(context)) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl) // Your full TMDB image URL
                .crossfade(true)
                .placeholder(R.drawable.movie_placeholder100) // Placeholder drawable
                .error(R.drawable.movie_placeholder100)       // Show this if loading fails
                .build(),
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier,
            imageLoader = ImageLoader.Builder(context)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.movie_placeholder100),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}