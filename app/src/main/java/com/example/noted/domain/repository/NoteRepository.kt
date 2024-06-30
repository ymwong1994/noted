package com.example.noted.domain.repository

import com.example.noted.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun upsertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun deleteNotes(notes: List<Note>)
}