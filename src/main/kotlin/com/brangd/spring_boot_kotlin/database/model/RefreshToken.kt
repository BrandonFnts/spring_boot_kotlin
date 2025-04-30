package com.brangd.spring_boot_kotlin.database.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("refresh_tokens")
data class RefreshToken(
    @Id val id: ObjectId = ObjectId.get(),
    val userId: ObjectId,
    @Indexed(expireAfter = "0s")
    val expiresAt: Instant,
    val createdAt: Instant = Instant.now(),
    val hashedToken: String
)