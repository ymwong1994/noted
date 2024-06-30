package com.example.noted.domain.usecase

import com.example.noted.domain.model.Note
import com.example.noted.domain.repository.NoteRepository

class DeleteNotesUseCase(private val repository: NoteRepository) {
    suspend fun execute(notes: List<Note>) = repository.deleteNotes(notes)
}
