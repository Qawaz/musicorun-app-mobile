package com.musicorumapp.mobile.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.musicorumapp.mobile.authentication.AuthenticationPreferences
import com.musicorumapp.mobile.state.models.AuthenticationViewModel
import com.musicorumapp.mobile.ui.navigation.MainNavigationHost

@Composable
fun MainApp(
    authenticationViewModel: AuthenticationViewModel?,
    authPrefs: AuthenticationPreferences?,
    navController: NavHostController
) {
    val token = authPrefs?.getLastfmSessionToken() ?: "."
    val systemUiController = rememberSystemUiController()

    systemUiController.setNavigationBarColor(
        Color.Transparent,
        navigationBarContrastEnforced = false
    )

    MainNavigationHost(
        navController = navController,
        authenticationViewModel = authenticationViewModel,
    )
}