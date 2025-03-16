package com.sb.home.presentation.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sb.core.resources.AppRes
import com.sb.core.resources.theme.EqualizerTheme

@Composable
fun HomeButton(
    text: String,
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.elevatedButtonColors().copy(
            containerColor = AppRes.colors.secondary,
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.invoke()
            Text(
                text = text,
                color = AppRes.colors.primary,
                fontSize = 18.sp,
                style = AppRes.type.gilroySemibold
            )
        }
    }
}

@Preview
@Composable
private fun HomeButtonPreview() {
    EqualizerTheme {
        HomeButton(
            text = "Начать запись",
            onClick = {},
            icon = {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = AppRes.icons.play,
                    contentDescription = null,
                    tint = AppRes.colors.primary
                )
            }
        )
    }
}