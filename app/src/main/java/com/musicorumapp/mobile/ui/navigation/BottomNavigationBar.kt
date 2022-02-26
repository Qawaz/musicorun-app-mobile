package com.musicorumapp.mobile.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsPadding
import com.musicorumapp.mobile.Constants
import com.musicorumapp.mobile.ui.theme.md_theme_dark_surface

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    if (pagesWithBottomBar.contains(currentRoute(navController = navController).orEmpty())) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 1.dp,

            ) {
            Box(
                modifier = Modifier.navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    mainPages.forEach {
                        val title = stringResource(id = it.titleResource)
                        val currentRoute = currentRoute(navController = navController)
                        val selected = currentRoute == it.name

                        NavigationBarItem(
                            selected = selected,

                            icon = {
                                if (it.icon.material != null) Icon(
                                    imageVector = it.icon.material,
                                    contentDescription = title
                                )
                                else if (it.icon.drawable != null) Icon(
                                    painter = painterResource(id = it.icon.drawable),
                                    contentDescription = title
                                )
                            },
                            onClick = {
                                if (!selected) {
                                    navController.navigate(it.name) {
                                        popUpTo(navController.graph.startDestinationRoute.orEmpty()) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            label = {
                                Text(
                                    title,
                                    overflow = TextOverflow.Ellipsis,
                                    softWrap = false,
//                            fontSize = 10.sp
                                )
                            },
                            alwaysShowLabel = false,

                            )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(navController = rememberNavController())
}