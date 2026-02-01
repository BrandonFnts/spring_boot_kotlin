package com.brangd.spring_boot_kotlin.controllers

import com.brangd.spring_boot_kotlin.database.models.TagModel
import com.brangd.spring_boot_kotlin.database.repository.TagRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tags")
class TagController(
    private val tagRepository: TagRepository
) {
    data class TagResponse(
        val id: String,
        val name: String,
        val color: Long,
        val description: String?
    )

    private fun TagModel.toResponse() = TagResponse(
        id = this.id.toHexString(),
        name = this.name,
        color = this.color,
        description = this.description
    )

    // --- ENDPOINTS ---

    @GetMapping
    fun getAllTags(): List<TagResponse> {
        return tagRepository.findAll().map { it.toResponse() }
    }
}