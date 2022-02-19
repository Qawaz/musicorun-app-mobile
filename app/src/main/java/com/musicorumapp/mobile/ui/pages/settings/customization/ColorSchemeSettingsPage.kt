package com.musicorumapp.mobile.ui.pages.settings.customization

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.insets.statusBarsPadding
import com.musicorumapp.mobile.R
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContext
import com.musicorumapp.mobile.state.models.settings.customization.ColorSchemeOption
import com.musicorumapp.mobile.state.models.settings.customization.ColorSchemeSettingsViewModel
import com.musicorumapp.mobile.ui.components.settings.SettingsListItem
import com.musicorumapp.mobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSchemeSettingsPage(
    colorSchemeSettingsViewModel: ColorSchemeSettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val navigationContext = LocalNavigationContext.current
    val colorSchemeOption = colorSchemeSettingsViewModel.colorSchemeOption
    val dynamicColor = dynamicDarkColorScheme(context).primary

//    val colorScheme = when (colorSchemeOption) {
//        ColorSchemeOption.MUSICORUM -> DarkThemeColors
//        ColorSchemeOption.PROFILE -> createThemeFromColor(colorSchemeSettingsViewModel.avatarImagePredominantColor)
//        ColorSchemeOption.SYSTEM -> createThemeFromColor(dynamicColor)
//    }
//
//    val previewColorPrimary = animateColorAsState(
//        colorScheme.primary
//    )
//
//    val previewColorOnPrimary = animateColorAsState(
//        colorScheme.onPrimary
//    )

    val previewColorPrimary = MaterialTheme.colorScheme.primary
    val previewColorOnPrimary = MaterialTheme.colorScheme.primary

    LaunchedEffect("Start") {
        colorSchemeSettingsViewModel.parseAvatarColor(context)
    }

    Column {
        Column(
            modifier = Modifier.background(md_theme_dark_surfaceVariant)
        ) {
            Spacer(modifier = Modifier.statusBarsPadding())
            SmallTopAppBar(
                title = { Text(stringResource(R.string.settings_customization_scheme)) },
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
                OutlinedCard(
                    modifier = Modifier
                        .padding(PaddingSpacing.HorizontalMainPadding)
                        .fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PaddingSpacing.HorizontalMainPadding),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            colors = buttonColors(
                                containerColor = previewColorPrimary,
                                contentColor = previewColorOnPrimary
                            ),
                            onClick = { /*TODO*/ }) {
                            Text(stringResource(R.string.settings_customization_scheme_example))
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(previewColorOnPrimary)
                                .size(32.dp)
                        ) {
                            Icon(
                                AppMaterialIcons.Person,
                                contentDescription = stringResource(R.string.settings_customization_scheme_example),
                                tint = previewColorPrimary,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            item {
                SettingsListItem(
                    primaryText = stringResource(R.string.settings_customization_scheme_musicorum),
                    secondaryText = stringResource(R.string.settings_customization_scheme_musicorum_description),
                    icon = {
                        RadioButton(
                            colors = RadioButtonDefaults.colors(
                                selectedColor = previewColorPrimary
                            ),
                            selected = colorSchemeOption == ColorSchemeOption.MUSICORUM,
                            onClick = { colorSchemeSettingsViewModel.changeTo(ColorSchemeOption.MUSICORUM) })
                    },
                    modifier = Modifier.clickable {
                        colorSchemeSettingsViewModel.changeTo(ColorSchemeOption.MUSICORUM)
                    }
                )
            }

            item {
                SettingsListItem(
                    primaryText = stringResource(R.string.settings_customization_scheme_profile),
                    secondaryText = stringResource(R.string.settings_customization_scheme_profile_description),
                    icon = {
                        RadioButton(
                            colors = RadioButtonDefaults.colors(
                                selectedColor = previewColorPrimary
                            ),
                            selected = colorSchemeOption == ColorSchemeOption.PROFILE,
                            onClick = { colorSchemeSettingsViewModel.changeTo(ColorSchemeOption.PROFILE) })
                    },
                    modifier = Modifier.clickable {
                        colorSchemeSettingsViewModel.changeTo(ColorSchemeOption.PROFILE)
                    }
                )
            }

            item {
                SettingsListItem(
                    primaryText = stringResource(R.string.settings_customization_scheme_system),
                    secondaryText = stringResource(R.string.settings_customization_scheme_system_description),
                    icon = {
                        RadioButton(
                            colors = RadioButtonDefaults.colors(
                                selectedColor = previewColorPrimary
                            ),
                            selected = colorSchemeOption == ColorSchemeOption.SYSTEM,
                            onClick = { colorSchemeSettingsViewModel.changeTo(ColorSchemeOption.SYSTEM) })
                    },
                    modifier = Modifier.clickable {
                        colorSchemeSettingsViewModel.changeTo(ColorSchemeOption.SYSTEM)
                    }
                )
            }
        }
    }
}