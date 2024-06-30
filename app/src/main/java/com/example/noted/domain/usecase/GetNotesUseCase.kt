package com.example.noted.domain.usecase

import com.example.noted.domain.model.Note
import com.example.noted.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow


class GetNotesUseCase(private val repository: NoteRepository) {
    fun execute(): Flow<List<Note>> = repository.getAllNotes()
}