package com.example.models

import kotlinx.serialization.Serializable
@Serializable
data class RevokeTokenRequest(
    val token: String
)