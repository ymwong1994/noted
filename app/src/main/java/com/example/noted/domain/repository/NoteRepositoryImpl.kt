package com.example.noted.domain.repository

import com.example.noted.data.NoteDao
import com.example.noted.domain.model.Note
import kotlinx.coroutines.flow.Flow


class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    override suspend fun upsertNote(note: Note) = noteDao.upsertNote(note)
    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
    override suspend fun deleteNotes(notes: List<Note>) = noteDao.deleteNotes(notes)
}