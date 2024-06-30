package com.example.noted.state

import com.example.noted.domain.model.Note

sealed class NoteState {
    object Loading : NoteState()
    data class Success(val notes: List<Note>) : NoteState()
    data class Error(val error: String) : NoteState()
}