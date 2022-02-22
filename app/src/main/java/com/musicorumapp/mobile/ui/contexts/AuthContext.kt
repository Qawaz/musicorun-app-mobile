package com.musicorumapp.mobile.ui.contexts

import androidx.compose.runtime.compositionLocalOf
import com.musicorumapp.mobile.api.models.User

class LocalAuthContent(
    val user: User? = null
)

val LocalAuth = compositionLocalOf {
    LocalAuthContent()
}