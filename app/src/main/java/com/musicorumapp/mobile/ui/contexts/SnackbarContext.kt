package com.musicorumapp.mobile.ui.contexts

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

class LocalSnackbarContextContent(
    val snackbarHostState: SnackbarHostState? = null
) {
    suspend fun showSnackBar (message: String) = snackbarHostState?.showSnackbar(message)
}


val LocalSnackbarContext = compositionLocalOf {
    LocalSnackbarContextContent()
}