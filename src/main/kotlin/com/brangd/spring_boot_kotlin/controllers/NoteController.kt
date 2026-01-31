package com.brangd.spring_boot_kotlin.controllers

import com.brangd.spring_boot_kotlin.database.models.NoteModel
import com.brangd.spring_boot_kotlin.database.models.toResponse
import com.brangd.spring_boot_kotlin.database.repository.NoteRepository
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
    private val noteRepository: NoteRepository
) {

    data class NoteRequest(
        @field:NotBlank(message = "Title can't be blank.")
        val title: String,
        val content: String,
        val color: Long
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt: Instant
    )

    fun NoteModel.toResponse() = NoteResponse(
        id = this.id.toHexString(),
        title = this.title,
        content = this.content,
        color = this.color,
        createdAt = this.createdAt
    )

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

    // --- ENDPOINTS ---

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody body: NoteRequest): NoteResponse {
        val newNote = NoteModel(
            id = ObjectId.get(),
            title = body.title,
            content = body.content,
            color = body.color,
            createdAt = Instant.now(),
            ownerId = getOwnerId()
        )
        return noteRepository.save(newNote).toResponse()
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @Valid @RequestBody body: NoteRequest
    ): NoteResponse {
        val existingNote = findNoteAndValidateOwner(id)

        val updatedNote = existingNote.copy(
            title = body.title,
            content = body.content,
            color = body.color
        )

        return noteRepository.save(updatedNote).toResponse()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): NoteResponse {
        val note = findNoteAndValidateOwner(id)
        return note.toResponse()
    }

    @GetMapping
    fun getAllMyNotes(): List<NoteResponse> {
        return noteRepository.findByOwnerId(getOwnerId()).map { it.toResponse() }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteById(@PathVariable id: String) {
        val note = findNoteAndValidateOwner(id)
        noteRepository.deleteById(note.id)
    }
}