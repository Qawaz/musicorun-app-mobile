package com.musicorumapp.mobile.ui.contexts

import android.content.Context
import android.icu.text.CompactDecimalFormat
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.musicorumapp.mobile.api.models.User

class CompactDecimalFormatContent(
    context: Context? = null
) {
    private val locale = context?.resources?.configuration?.locales?.get(0)
    val instance: CompactDecimalFormat = CompactDecimalFormat.getInstance(locale, CompactDecimalFormat.CompactStyle.SHORT)
}

val LocalCompactDecimalFormatContext = compositionLocalOf {
    CompactDecimalFormatContent()
}