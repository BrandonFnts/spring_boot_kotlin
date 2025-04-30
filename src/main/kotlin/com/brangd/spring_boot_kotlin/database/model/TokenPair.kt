package com.brangd.spring_boot_kotlin.database.model

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
