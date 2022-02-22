package com.musicorumapp.mobile.ui.components

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.musicorumapp.mobile.Constants
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.authentication.AuthenticationPreferences
import com.musicorumapp.mobile.state.models.AuthenticationViewModel
import com.musicorumapp.mobile.ui.LoginScreen
import com.musicorumapp.mobile.ui.contexts.*
import com.musicorumapp.mobile.ui.navigation.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationRouter(
    token: String?,
    authenticationPreferences: AuthenticationPreferences
) {
    val authenticationViewModel: AuthenticationViewModel = viewModel()

    val authenticationState by authenticationViewModel.authenticationValidationState.observeAsState(
        AuthenticationViewModel.State.NONE
    )

    Log.i(LogTag, LocalWindowInsets.current.navigationBars.layoutInsets.top.toString())

    LaunchedEffect(token) {
        when {
            authenticationPreferences.checkIfTokenExists() -> {
                authenticationViewModel.fetchUser {
                    Log.e(Constants.LOG_TAG, it)
                }
            }
            token != null -> {
                authenticationViewModel.authenticateFromToken(token) {
                    Log.e(
                        Constants.LOG_TAG,
                        it
                    )
                }
            }
            else -> authenticationViewModel.setAuthenticationValidationState(
                AuthenticationViewModel.State.LOGGED_OUT
            )
        }
    }

    val loggedIn =
        (authenticationState == AuthenticationViewModel.State.AUTHENTICATING || authenticationState == AuthenticationViewModel.State.LOGGED_IN)

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    CompositionLocalProvider(
        LocalAuth provides LocalAuthContent(authenticationViewModel.user.value),
        LocalSnackbarContext provides LocalSnackbarContextContent(snackbarHostState),
        LocalCompactDecimalFormatContext provides CompactDecimalFormatContent(context)
    ) {
        Scaffold(
            bottomBar = {
                Crossfade(loggedIn) {
                    if (it) BottomNavigationBar(navController = navController)
                }
            },
            snackbarHost = {
                SnackbarHost(
                    modifier = Modifier.navigationBarsPadding(),
                    hostState = snackbarHostState
                )
            }
        ) {


            if (loggedIn)
                Box(modifier = Modifier.navigationBarsPadding()) {
                    MainApp(authenticationViewModel, authenticationPreferences, navController)
                }
            else if (authenticationState === AuthenticationViewModel.State.LOGGED_OUT)
                LoginScreen(authenticationViewModel, authenticationPreferences)


        }
    }

//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//
//        if (authenticationState == AuthenticationViewModel.State.LOGGED_OUT) {
//            Text("Logged out")
//        }
//    }
}