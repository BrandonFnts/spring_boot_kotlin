package com.brangd.spring_boot_kotlin.database.models

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
