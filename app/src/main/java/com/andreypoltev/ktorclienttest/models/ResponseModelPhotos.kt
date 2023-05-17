package com.andreypoltev.ktorclienttest.models

import kotlinx.serialization.Serializable

@Serializable
data class ResponseModelPhotos(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)