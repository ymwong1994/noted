package com.example.noted.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.noted.domain.model.Note
import com.example.noted.intent.NoteIntent
import com.example.noted.state.NoteState
import com.example.noted.presentation.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: NoteViewModel, navController: NavController) {
    val state by viewModel.state.collectAsState()
    var selectedNotes by remember { mutableStateOf(setOf<Note>()) }
    var selectionMode by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = selectionMode) {
        selectedNotes = emptySet()
        selectionMode = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") }
            )
        },
        floatingActionButton = {
            if (!selectionMode) {
                FloatingActionButton(onClick = {
                    navController.navigate("createNote")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                }
            }
        },
        bottomBar = {
            if (selectionMode) {
                BottomNavigationBar(
                    selectedNotes = selectedNotes,
                    onDeleteSelected = {
                        viewModel.handleIntent(NoteIntent.DeleteNotes(selectedNotes.toList()))
                        selectedNotes = emptySet()
                        selectionMode = false
                    },
                    onSelectAll = {
                        selectedNotes = (state as? NoteState.Success)?.notes?.toSet() ?: emptySet()
                    },
                    onCancel = {
                        selectedNotes = emptySet()
                        selectionMode = false
                    }
                )
            }
        }
    ) { paddingValues ->
        when (state) {
            is NoteState.Loading -> {
                // Show loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is NoteState.Success -> {
                val notes = (state as NoteState.Success).notes
                if(notes.isEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Note to display, create one to display here!",
                            style = MaterialTheme.typography.bodyLarge)
                    }
                }
                else{
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(128.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        items(notes) { note ->
                            NoteItem(
                                note = note,
                                navController = navController,
                                isSelected = selectedNotes.contains(note),
                                onSelectNote = { selectedNote ->
                                    if (selectedNotes.contains(selectedNote)) {
                                        selectedNotes = selectedNotes - selectedNote
                                    } else {
                                        selectedNotes = selectedNotes + selectedNote
                                    }
                                },
                                onLongClick = {
                                    selectionMode = true
                                    if (!selectedNotes.contains(note)) {
                                        selectedNotes = selectedNotes + note
                                    }
                                },
                                selectionMode = selectionMode
                            )
                        }
                    }
                }
            }
            is NoteState.Error -> {
                val message = (state as NoteState.Error).error
                // Show error message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    navController: NavController,
    isSelected: Boolean,
    onSelectNote: (Note) -> Unit,
    onLongClick: () -> Unit,
    selectionMode: Boolean
) {
    val shape = MaterialTheme.shapes.extraSmall

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .width(160.dp) // Fixed width for each card
            .height(160.dp) // Fixed height for each card
            .combinedClickable(
                onClick = {
                    if (selectionMode) {
                        onSelectNote(note)
                    } else {
                        navController.navigate("noteDetail/${note.id}")
                    }
                },
                onLongClick = onLongClick
            ),
        shape = shape, // Ensure rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.content,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
            }
            if (selectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onSelectNote(note) },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedNotes: Set<Note>,
    onDeleteSelected: () -> Unit,
    onSelectAll: () -> Unit,
    onCancel: () -> Unit
) {
    BottomAppBar {
        Text(text = "${selectedNotes.size} selected", modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onSelectAll) {
            Icon(imageVector = Icons.Default.List, contentDescription = "Select All")
        }
        IconButton(onClick = onDeleteSelected) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Selected")
        }
        IconButton(onClick = onCancel) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
        }
    }
}
