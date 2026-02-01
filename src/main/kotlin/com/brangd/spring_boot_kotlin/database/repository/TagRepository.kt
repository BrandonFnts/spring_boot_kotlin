package com.brangd.spring_boot_kotlin.database.repository

import com.brangd.spring_boot_kotlin.database.models.TagModel
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : MongoRepository<TagModel, ObjectId> {
    fun findByName(name: String): TagModel?
}