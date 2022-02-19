package com.musicorumapp.mobile.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musicorumapp.mobile.ui.theme.PaddingSpacing

@Composable
fun SettingsListItem(
    primaryText: String,
    secondaryText: String? = null,
    icon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .composed { modifier }
            .fillMaxWidth()
            .padding(
                horizontal = PaddingSpacing.HorizontalMainPadding,
                vertical = PaddingSpacing.SmallPadding
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(
                    modifier = Modifier.padding(
                        horizontal = 6.dp
                    )
                )
            }

            Column {
                Text(
                    primaryText,
                    fontWeight = FontWeight.Medium
                )
                if (!secondaryText.isNullOrEmpty()) {
                    Text(
                        secondaryText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}