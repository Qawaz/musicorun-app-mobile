package com.musicorumapp.mobile.state.models.settings.customization

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.state.SessionState
import com.musicorumapp.mobile.ui.theme.md_theme_dark_primary
import com.musicorumapp.mobile.utils.fetchImageColors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ColorSchemeOption {
    MUSICORUM, PROFILE, SYSTEM
}

@HiltViewModel
class ColorSchemeSettingsViewModel @Inject constructor(
    private val sessionState: SessionState
) : ViewModel() {
    var colorSchemeOption by mutableStateOf(ColorSchemeOption.MUSICORUM)
    var avatarImagePredominantColor by mutableStateOf(Color.White)

    fun changeTo(option: ColorSchemeOption) {
        colorSchemeOption = option
    }

    fun parseAvatarColor(context: Context) {
        val avatarImage = sessionState.currentUser?.images?.bestImage

        if (!avatarImage.isNullOrEmpty()) {
            viewModelScope.launch {
                val palette = fetchImageColors(context, avatarImage)

                if (palette != null) {
                    Log.i(
                        LogTag,
                        palette.lightVibrantSwatch?.hsl?.joinToString { "," } ?: "No swatch")
                    palette.lightVibrantSwatch?.rgb?.let {
                        avatarImagePredominantColor = (Color(it))
                    }
                }
            }
        }
    }
}