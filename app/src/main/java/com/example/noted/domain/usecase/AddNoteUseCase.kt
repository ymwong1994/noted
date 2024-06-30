package com.example.noted.domain.usecase

import com.example.noted.domain.model.Note
import com.example.noted.domain.repository.NoteRepository

class AddNoteUseCase(private val repository: NoteRepository) {
    suspend fun execute(note: Note) = repository.upsertNote(note)
}
