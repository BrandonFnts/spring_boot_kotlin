package com.brangd.spring_boot_kotlin.database.models

import com.brangd.spring_boot_kotlin.controllers.NoteController
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("notes")
data class NoteModel(
    @Id val id: ObjectId = ObjectId.get(),
    val title: String,
    val content: String,
    val color: String,
    val tags: List<ObjectId> = emptyList(),
    val createdAt: Instant,
    val ownerId: ObjectId
)