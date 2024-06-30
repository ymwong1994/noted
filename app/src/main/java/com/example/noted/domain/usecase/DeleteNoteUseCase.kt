package com.example.noted.domain.usecase

import com.example.noted.domain.model.Note
import com.example.noted.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend fun execute(note: Note) = repository.deleteNote(note)
}
