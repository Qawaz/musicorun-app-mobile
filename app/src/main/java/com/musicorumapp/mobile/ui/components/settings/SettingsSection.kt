package com.musicorumapp.mobile.ui.components.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.musicorumapp.mobile.ui.theme.PaddingSpacing

@Composable
fun SettingsSection(
    name: String,
    items: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(
            vertical = PaddingSpacing.MediumPadding
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = PaddingSpacing.HorizontalMainPadding
            ),
        ) {
            Text(
                name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Divider(
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Spacer(modifier = Modifier.height(PaddingSpacing.SmallPadding))

        items()
    }
}