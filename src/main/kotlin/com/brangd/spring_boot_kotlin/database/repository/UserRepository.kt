package com.brangd.spring_boot_kotlin.database.repository

import com.brangd.spring_boot_kotlin.database.models.UserModel
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<UserModel, ObjectId> {
    fun findByEmail(email: String): UserModel?
}