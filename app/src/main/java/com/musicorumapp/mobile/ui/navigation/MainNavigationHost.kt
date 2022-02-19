package com.musicorumapp.mobile.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContext
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContextContent
import com.musicorumapp.mobile.state.models.AuthenticationViewModel
import com.musicorumapp.mobile.ui.components.PageAnimation
import com.musicorumapp.mobile.ui.pages.artist.ArtistPage
import com.musicorumapp.mobile.ui.pages.DiscoverPage
import com.musicorumapp.mobile.ui.pages.ExtendedPageableListPage
import com.musicorumapp.mobile.ui.pages.HomePage
import com.musicorumapp.mobile.ui.pages.settings.SettingsMainPage
import com.musicorumapp.mobile.ui.pages.settings.customization.ColorSchemeSettingsPage

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
        NavHost(navController = navController, startDestination = BottomPage.Home.name) {
            composable(BottomPage.Home.name) {
                PageAnimation {
                    HomePage(
                        authenticationViewModel = authenticationViewModel
                    )
                }
            }
            composable(BottomPage.Scrobbling.name) {
                PageAnimation {
                    Text("Scrobble")
                }
            }
            composable(BottomPage.Charts.name) {
                PageAnimation {
                    Text("Charts")
                }
            }
            composable(BottomPage.Profile.name) {
                PageAnimation {
                    Text("Profile")
                }
            }

            composable(
                "artist/{storeId}",
                arguments = listOf(navArgument("storeId") { type = NavType.StringType })
            ) {
                val storeId = it.arguments?.getString("storeId")
                val artist = LocalNavigationContext.current.artistsStore[storeId]
                PageAnimation {
                    ArtistPage(artist = artist)
                }
            }

            composable(
                ComposableRoutes.ExtendedList("{storeId}"),
                arguments = listOf(navArgument("storeId") { type = NavType.StringType })
            ) {
                val storeId = it.arguments?.getString("storeId")
                val storeItem = LocalNavigationContext.current.pagingControllerStore[storeId]
                PageAnimation {
                    ExtendedPageableListPage(title = storeItem!!.title, from = storeItem.from, controller = storeItem.controller)
                }
            }

            composable(ComposableRoutes.SettingsMainPage) {
                PageAnimation {
                    SettingsMainPage()
                }
            }

            composable(ComposableRoutes.ColorSchemeSettingsPage) {
                PageAnimation {
                    ColorSchemeSettingsPage()
                }
            }

            composable(ComposableRoutes.Search) {
                PageAnimation {
                    DiscoverPage()
                }
            }
        }
    }
}

object ComposableRoutes {
    val ExtendedList = { it: String -> "extendedList/${it}" }
    val Search = "search"
    val SettingsMainPage = "settings"
    val ColorSchemeSettingsPage = "settings/colorScheme"
}