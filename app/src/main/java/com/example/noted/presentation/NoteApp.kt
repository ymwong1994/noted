package com.example.noted.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.noted.presentation.viewmodel.NoteViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NoteApp(viewModel: NoteViewModel) {
    val navController = rememberNavController()
    NoteNavHost(navController = navController, viewModel = viewModel)
}

@Composable
fun NoteNavHost(navController: NavHostController, viewModel: NoteViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(viewModel, navController)
        }
        composable("createNote") {
            CreateNoteScreen(viewModel, navController)
        }
        composable("noteDetail/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toInt() ?: 0
            NoteDetailScreen(noteId, viewModel, navController)
        }
    }
}