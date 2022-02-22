package com.musicorumapp.mobile

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.musicorumapp.mobile.authentication.AuthenticationPreferences
import com.musicorumapp.mobile.ui.components.AuthenticationRouter
import com.musicorumapp.mobile.ui.theme.MusicorumTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(Constants.AUTH_PREFS_KEY, MODE_PRIVATE)
        val authPrefs = AuthenticationPreferences(prefs)

        var token: String? = null

        if (
            !authPrefs.checkIfTokenExists()
            && intent != null
            && intent.data != null
            && intent.action == "android.intent.action.VIEW"
        ) {
            val intentToken = intent.data?.getQueryParameter("token")
            if (intentToken != null) {
                token = intentToken
            }
        }

        // Necessary for accompanist -> https://google.github.io/accompanist/insets/#something-not-working
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setTheme(R.style.Theme_Musicorum_NoActionBar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }

        Log.i(LogTag, token ?: "")

        setContent {
            MusicorumTheme {
                ProvideWindowInsets {
                    AuthenticationRouter(
                        token = token,
                        authenticationPreferences = authPrefs
                    )
                }
            }
        }
    }
}