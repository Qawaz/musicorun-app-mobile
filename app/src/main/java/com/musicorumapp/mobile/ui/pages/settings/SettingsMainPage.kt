package com.musicorumapp.mobile.ui.pages.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.musicorumapp.mobile.R
import com.musicorumapp.mobile.ui.contexts.LocalAuth
import com.musicorumapp.mobile.ui.contexts.LocalAuthContent
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContext
import com.musicorumapp.mobile.ui.components.settings.SettingsListItem
import com.musicorumapp.mobile.ui.components.settings.SettingsSection
import com.musicorumapp.mobile.ui.navigation.ComposableRoutes
import com.musicorumapp.mobile.ui.theme.AppMaterialIcons
import com.musicorumapp.mobile.ui.theme.md_theme_dark_surfaceVariant

@Composable
fun SettingsMainPage() {
    val navigationContext = LocalNavigationContext.current
    val authContext = LocalAuth.current

    Column {
        Column(
            modifier = Modifier.background(md_theme_dark_surfaceVariant)
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            SmallTopAppBar(
                title = { Text(stringResource(id = R.string.settings)) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navigationContext.navigationController?.popBackStack()
                    }) {
                        Icon(
                            AppMaterialIcons.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                SettingsSection(name = stringResource(R.string.settings_customization_name)) {
                    SettingsListItem(
                        primaryText = stringResource(R.string.settings_customization_scheme),
                        secondaryText = stringResource(R.string.settings_customization_scheme_description),
                        modifier = Modifier.clickable {
                            navigationContext.navigationController?.navigate(ComposableRoutes.ColorSchemeSettingsPage)
                        },
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.ic_round_palette_24),
                                contentDescription = stringResource(R.string.settings_customization_scheme),
                                modifier = Modifier.size(32.dp)
                            )
                        },
                    )
                }
            }
        }
    }
}