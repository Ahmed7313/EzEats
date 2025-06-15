package com.trend.thecontent.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.thub.uadadvertisor.presentation.components.LoadingAnimation

@Composable
fun LoadingView(isLoading: Boolean) {
    var showDialog by remember { mutableStateOf(false) }

    if (isLoading) {
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {

            LoadingAnimation(modifier = Modifier.size(200.dp))
        }
    }
}

@Composable
fun LoadingViewState(isLoading: Boolean) {
    var showDialog by remember { mutableStateOf(false) }

    if (isLoading) {
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {

            LoadingAnimation(modifier = Modifier.size(200.dp))
        }
    }
}

@Composable
fun LoadingViewFullScreen(isLoading: Boolean) {
    var showDialog by remember { mutableStateOf(false) }

    if (isLoading) {
        Dialog(
            onDismissRequest = { showDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
            ) {
                LoadingAnimation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}