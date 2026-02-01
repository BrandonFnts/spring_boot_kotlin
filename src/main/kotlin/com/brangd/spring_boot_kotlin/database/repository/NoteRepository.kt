package com.brangd.spring_boot_kotlin.database.repository

import com.brangd.spring_boot_kotlin.database.models.NoteModel
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository: MongoRepository<NoteModel, ObjectId> {
    fun findByOwnerId(ownerId: ObjectId): List<NoteModel>
}