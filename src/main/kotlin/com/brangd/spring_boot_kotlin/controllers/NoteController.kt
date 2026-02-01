package com.brangd.spring_boot_kotlin.controllers

import com.brangd.spring_boot_kotlin.database.models.NoteModel
import com.brangd.spring_boot_kotlin.database.repository.NoteRepository
import com.brangd.spring_boot_kotlin.database.repository.TagRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    private val noteRepository: NoteRepository,
    private val tagRepository: TagRepository
) {
    data class NoteRequest(
        @field:NotBlank(message = "Title can't be blank.")
        val title: String,
        val content: String,
        val tagIds: List<String> = emptyList()
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val tags: List<TagController.TagResponse>,
        val createdAt: Instant
    )

    // --- HELPERS ---

    private fun getOwnerId(): ObjectId {
        val principal = SecurityContextHolder.getContext().authentication.principal as String
        return ObjectId(principal)
    }

    private fun findNoteAndValidateOwner(noteId: String): NoteModel {
        if (!ObjectId.isValid(noteId)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID format")
        }
        val note = noteRepository.findById(ObjectId(noteId))
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found") }

        if (note.ownerId != getOwnerId()) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found")
        }
        return note
    }

    private fun mapNotesToResponse(notes: List<NoteModel>): List<NoteResponse> {
        if (notes.isEmpty()) return emptyList()

        val allTagIds = notes.flatMap { it.tags }.distinct()

        val tagsMap = tagRepository.findAllById(allTagIds).associateBy { it.id }

        return notes.map { note ->
            val noteTags = note.tags.mapNotNull { tagId ->
                tagsMap[tagId]?.let { tagModel ->
                    TagController.TagResponse(
                        id = tagModel.id.toHexString(),
                        name = tagModel.name,
                        color = tagModel.color,
                        description = tagModel.description
                    )
                }
            }

            NoteResponse(
                id = note.id.toHexString(),
                title = note.title,
                content = note.content,
                tags = noteTags,
                createdAt = note.createdAt
            )
        }
    }

    private fun mapNoteToResponse(note: NoteModel): NoteResponse {
        return mapNotesToResponse(listOf(note)).first()
    }

    // --- ENDPOINTS ---

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody body: NoteRequest): NoteResponse {
        val tagObjectIds = body.tagIds.map { ObjectId(it) }

        val newNote = NoteModel(
            id = ObjectId.get(),
            title = body.title,
            content = body.content,
            tags = tagObjectIds,
            createdAt = Instant.now(),
            ownerId = getOwnerId()
        )
        return mapNoteToResponse(noteRepository.save(newNote))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @Valid @RequestBody body: NoteRequest
    ): NoteResponse {
        val existingNote = findNoteAndValidateOwner(id)
        val tagObjectIds = body.tagIds.map { ObjectId(it) }

        val updatedNote = existingNote.copy(
            title = body.title,
            content = body.content,
            tags = tagObjectIds
        )
        return mapNoteToResponse(noteRepository.save(updatedNote))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): NoteResponse {
        val note = findNoteAndValidateOwner(id)
        return mapNoteToResponse(note)
    }

    @GetMapping
    fun getAllMyNotes(): List<NoteResponse> {
        val notes = noteRepository.findByOwnerId(getOwnerId())
        return mapNotesToResponse(notes)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: String) {
        val note = findNoteAndValidateOwner(id)
        noteRepository.deleteById(note.id)
    }
}