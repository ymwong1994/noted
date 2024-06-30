package com.example.noted.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.noted.intent.NoteIntent
import com.example.noted.state.NoteState
import com.example.noted.utils.getCurrentTime
import com.example.noted.presentation.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(noteId: Int, viewModel: NoteViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    val note = (state as? NoteState.Success)?.notes?.find { it.id == noteId }

    note?.let {
        var title by remember { mutableStateOf(note.title) }
        var content by remember { mutableStateOf(note.content) }
        var isTitleError by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Note") },
                    actions = {
                        IconButton(onClick = {
                            viewModel.handleIntent(NoteIntent.DeleteNote(note))
                            navController.navigateUp()
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    TextField(
                        value = title,
                        onValueChange = {
                            title = it
                            if(isTitleError){
                                isTitleError = false
                            }
                        },
                        label = { Text("Title") },
                        isError = isTitleError,
                        keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if(isTitleError){
                        Text(text = "Title cannot be empty",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content") },
                        keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if(title.isEmpty()){
                                isTitleError = true
                            }
                            else{
                                val updatedNote = note.copy(
                                    title = title,
                                    content = content,
                                    updatedAt = getCurrentTime()
                                )
                                viewModel.handleIntent(NoteIntent.UpdateNote(updatedNote))
                                navController.navigateUp()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        )
    } ?: run {
        // Show a message if the note is not found
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Edit Note") })
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Note not found", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }
}
