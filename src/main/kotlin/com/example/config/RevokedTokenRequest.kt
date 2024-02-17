package com.example.config

import kotlinx.serialization.Serializable

@Serializable
data class RevokedTokenRequest(
    val token:String
)
