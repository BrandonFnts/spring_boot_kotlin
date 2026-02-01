package com.brangd.spring_boot_kotlin.database.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("tags")
data class TagModel(
    @Id val id: ObjectId = ObjectId.get(),
    val name: String,
    val color: Long,
    val description: String? = null
)
