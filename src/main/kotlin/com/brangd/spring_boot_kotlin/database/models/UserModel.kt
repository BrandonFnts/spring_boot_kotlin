package com.brangd.spring_boot_kotlin.database.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class UserModel(
    @Id val id: ObjectId = ObjectId(),
    val email: String,
    val hashedPassword: String
)
