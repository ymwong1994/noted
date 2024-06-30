package com.example.noted.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.noted.domain.usecase.AddNoteUseCase
import com.example.noted.domain.usecase.DeleteNoteUseCase
import com.example.noted.domain.usecase.DeleteNotesUseCase
import com.example.noted.domain.usecase.GetNotesUseCase
import com.example.noted.domain.usecase.UpdateNoteUseCase
import com.example.noted.intent.NoteIntent
import com.example.noted.state.NoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(
    application: Application,
    private val getNotesUseCase: GetNotesUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val deleteNotesUseCase: DeleteNotesUseCase
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<NoteState>(NoteState.Loading)
    val state: StateFlow<NoteState> = _state

    init {
        handleIntent(NoteIntent.LoadNotes)
    }

    fun handleIntent(intent: NoteIntent) {
        when (intent) {
            is NoteIntent.LoadNotes -> {
                viewModelScope.launch {
                    getNotesUseCase.execute().collect { notes ->
                        _state.value = NoteState.Success(notes)
                    }
                }
            }
            is NoteIntent.AddNote -> {
                viewModelScope.launch {
                    addNoteUseCase.execute(intent.note)
                    handleIntent(NoteIntent.LoadNotes)
                }
            }
            is NoteIntent.UpdateNote -> {
                viewModelScope.launch {
                    updateNoteUseCase.execute(intent.note)
                    handleIntent(NoteIntent.LoadNotes)
                }
            }
            is NoteIntent.DeleteNote -> {
                viewModelScope.launch {
                    deleteNoteUseCase.execute(intent.note)
                    handleIntent(NoteIntent.LoadNotes)
                }
            }
            is NoteIntent.DeleteNotes -> {
                viewModelScope.launch {
                    deleteNotesUseCase.execute(intent.notes)
                    handleIntent(NoteIntent.LoadNotes)
                }
            }
        }
    }
}