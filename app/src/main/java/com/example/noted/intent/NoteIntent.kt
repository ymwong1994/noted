package com.example.noted.intent

import com.example.noted.domain.model.Note

sealed class NoteIntent {
    object LoadNotes : NoteIntent()
    data class AddNote(val note: Note) : NoteIntent()
    data class UpdateNote(val note: Note) : NoteIntent()
    data class DeleteNote(val note: Note) : NoteIntent()
    data class DeleteNotes(val notes: List<Note>) : NoteIntent()
}