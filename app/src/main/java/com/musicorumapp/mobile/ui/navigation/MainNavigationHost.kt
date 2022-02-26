package com.musicorumapp.mobile.ui.navigation

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContext
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContextContent
import com.musicorumapp.mobile.state.models.AuthenticationViewModel
import com.musicorumapp.mobile.ui.components.PageAnimation
import com.musicorumapp.mobile.ui.pages.artist.ArtistPage
import com.musicorumapp.mobile.ui.pages.DiscoverPage
import com.musicorumapp.mobile.ui.pages.ExtendedPageableListPage
import com.musicorumapp.mobile.ui.pages.HomePage
import com.musicorumapp.mobile.ui.pages.album.AlbumPage
import com.musicorumapp.mobile.ui.pages.settings.SettingsMainPage
import com.musicorumapp.mobile.ui.pages.settings.customization.ColorSchemeSettingsPage

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavigationHost(
    navController: NavHostController,
    authenticationViewModel: AuthenticationViewModel?
) {
    CompositionLocalProvider(
        LocalNavigationContext provides LocalNavigationContextContent(
            navController
        )
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = BottomPage.Home.name,
//            enterTransition = { expandVertically() },
//            exitTransition = { shrinkVertically() }
            enterTransition = {
                fadeIn() + slideInHorizontally(initialOffsetX = { -80 })
            },
            exitTransition = {
                fadeOut() + slideOutHorizontally(targetOffsetX = { 80 })
            }
        ) {
            composable(BottomPage.Home.name) {
                HomePage(
                    authenticationViewModel = authenticationViewModel
                )
            }
            composable(BottomPage.Scrobbling.name) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text("Scrobble")
                }
            }

            composable(BottomPage.Charts.name) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text("Charts")
                }
            }

            composable(BottomPage.Profile.name) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text("Profile")
                }
            }

            composable(
                "artist/{storeId}",
                arguments = listOf(navArgument("storeId") { type = NavType.StringType })
            ) {
                val storeId = it.arguments?.getString("storeId")
                val artist = LocalNavigationContext.current.artistsStore[storeId]
                ArtistPage(artist = artist)
            }

            composable(
                ComposableRoutes.ExtendedList("{storeId}"),
                arguments = listOf(navArgument("storeId") { type = NavType.StringType })
            ) {
                val storeId = it.arguments?.getString("storeId")
                val storeItem = LocalNavigationContext.current.pagingControllerStore[storeId]
                ExtendedPageableListPage(
                    title = storeItem!!.title,
                    from = storeItem.from,
                    controller = storeItem.controller
                )
            }

            composable(
                ComposableRoutes.AlbumPage("{storeId}"),
                arguments = listOf(navArgument("storeId") { type = NavType.StringType })
            ) {
                val storeId = it.arguments?.getString("storeId")
                AlbumPage(storeId)
            }

            composable(ComposableRoutes.SettingsMainPage) {
                SettingsMainPage()
            }

            composable(ComposableRoutes.ColorSchemeSettingsPage) {
                ColorSchemeSettingsPage()
            }

            composable(ComposableRoutes.Search) {
                DiscoverPage()
            }
        }
    }
}

object ComposableRoutes {
    val ExtendedList = { it: String -> "extendedList/${it}" }
    val AlbumPage = { it: String -> "album/${it}" }
    val Search = "search"
    val SettingsMainPage = "settings"
    val ColorSchemeSettingsPage = "settings/colorScheme"
}