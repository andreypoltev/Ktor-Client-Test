package com.andreypoltev.ktorclienttest.models

import kotlinx.serialization.Serializable

@Serializable
data class ResponseModelPosts(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
)