package com.rajotiyapawan.trackflix.domain.model

data class ConfigData(
    val change_keys: List<String>,
    val images: ImageConfig,
)

data class ImageConfig(
    val base_url: String,
    val secure_base_url: String,
    val backdrop_sizes: List<String>,
    val logo_sizes: List<String>,
    val poster_sizes: List<String>,
    val profile_sizes: List<String>,
    val still_sizes: List<String>
)
